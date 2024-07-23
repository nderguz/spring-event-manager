package org.example.eventmanager.events.domain;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.eventmanager.events.UniversalEventMapper;
import org.example.eventmanager.events.api.RequestEvent;
import org.example.eventmanager.events.db.EventEntity;
import org.example.eventmanager.events.api.EventSearchFilter;
import org.example.eventmanager.events.db.EventRepository;
import org.example.eventmanager.events.db.RegistrationRepository;
import org.example.eventmanager.location.domain.LocationService;
import org.example.eventmanager.security.entities.Roles;
import org.example.eventmanager.security.services.AuthenticationService;
import org.example.eventmanager.users.domain.User;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class EventService {

    private final LocationService locationService;
    private final EventRepository eventRepository;
    private final UniversalEventMapper universalEventMapper;
    private final AuthenticationService authenticationService;
    private final RegistrationRepository registrationRepository;

    @Transactional
    public EventDomain createEvent(RequestEvent eventToCreate) {

        var currentUser = authenticationService.getCurrentAuthenticatedUser();
        var locationInfo = locationService.getLocationById(eventToCreate.locationId());
        checkAvailableLocationDate(eventToCreate);
        if(locationInfo.capacity() < eventToCreate.maxPlaces()){
            throw new IllegalArgumentException("Capacity of location is: %s, but maxPlaces is: %s".formatted(locationInfo.capacity(), eventToCreate.maxPlaces()));
        }
        var eventEntity = new EventEntity(
                null,
                eventToCreate.locationId(),
                eventToCreate.name(),
                EventStatus.WAITING,
                currentUser.getId(),
                eventToCreate.maxPlaces(),
                eventToCreate.cost(),
                eventToCreate.duration(),
                eventToCreate.date(),
                eventToCreate.date().plusMinutes(eventToCreate.duration()),
                List.of()
        );
        var savedEvent = eventRepository.save(eventEntity);
        return universalEventMapper.entityToDomain(savedEvent);
    }

    @Transactional
    public EventDomain getEventById(Long eventId) {
        return universalEventMapper.entityToDomain(eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found by id: %s".formatted(eventId))));
    }

    @Transactional
    public EventDomain deleteEvent(Long eventId) {
        var currentUser = authenticationService.getCurrentAuthenticatedUser();

        var eventToDelete = eventRepository.findById(eventId).orElseThrow(() -> new IllegalArgumentException("Event not found by id: %s".formatted(eventId)));

        if (currentUser.getRole().equals(Roles.ADMIN) || currentUser.getId().equals(eventToDelete.getOwnerId())) {
            if (eventToDelete.getStatus().equals(EventStatus.STARTED)) {
                throw new IllegalArgumentException("Event is already started");
            }
            if (eventToDelete.getStatus().equals(EventStatus.FINISHED)) {
                throw new IllegalArgumentException("Cannot close finished events");
            }
            eventToDelete.setStatus(EventStatus.CANCELLED);
            eventRepository.save(eventToDelete);
            registrationRepository.closeAllRegistrations(eventToDelete);
            return universalEventMapper.entityToDomain(eventToDelete);
        } else {
            throw new BadCredentialsException("Данный пользователь не может удалить событие");
        }
    }

    @Transactional
    public EventDomain updateEvent(Long eventId, RequestEvent eventToUpdate) {
        var currentUser = authenticationService.getCurrentAuthenticatedUser();
        var event = eventRepository.findById(eventId).orElseThrow(() -> new IllegalArgumentException("Event not found by id: %s".formatted(eventId)));
        checkCurrentUserCanModifyEvent(event, currentUser);
        checkAvailableLocationDate(eventToUpdate);
        if (currentUser.getRole().toString().equals(Roles.ADMIN.toString()) || currentUser.getId().equals(event.getOwnerId())) {
            event.setDateStart(eventToUpdate.date());
            event.setCost(eventToUpdate.cost());
            event.setDuration(eventToUpdate.duration());
            event.setMaxPlaces(eventToUpdate.maxPlaces());
            event.setLocationId(eventToUpdate.locationId());
            event.setName(eventToUpdate.name());
            event.setDateEnd(eventToUpdate.date().plusMinutes(eventToUpdate.duration()));
            var updatedEvent = eventRepository.save(event);
            return universalEventMapper.entityToDomain(updatedEvent);
        } else {
            throw new BadCredentialsException("Данный пользователь не может удалить событие");
        }
    }

    @Transactional
    public List<EventDomain> getUserEvents() {
        var currentUser = authenticationService.getCurrentAuthenticatedUser();
        return eventRepository.findAllUserEvents(currentUser.getId()).stream()
                .map(universalEventMapper::entityToDomain)
                .toList();
    }

    @Transactional
    public List<EventDomain> searchByFilter(EventSearchFilter searchFilter) {
        var foundEntities = eventRepository.findEvents(
                searchFilter.name(),
                searchFilter.placesMin(),
                searchFilter.placesMax(),
                searchFilter.dateStartAfter(),
                searchFilter.dateStartBefore(),
                searchFilter.costMin(),
                searchFilter.costMax(),
                searchFilter.durationMin(),
                searchFilter.durationMax(),
                searchFilter.locationId(),
                searchFilter.eventStatus()
        );
        return foundEntities.stream()
                .map(universalEventMapper::entityToDomain)
                .toList();
    }

    private void checkCurrentUserCanModifyEvent(EventEntity event, User currentUser){
        if(!event.getOwnerId().equals(currentUser.getId()) && !currentUser.getRole().equals(Roles.ADMIN)){
            throw new IllegalArgumentException("This user cannot modify this event");
        }
    }

    private void checkAvailableLocationDate(RequestEvent event){
        var events = eventRepository.findEventByDate(event.date(), event.date().plusMinutes(event.duration()), event.locationId());
        if(!events.isEmpty()){
            throw new IllegalArgumentException("Location already reserved at this date");
        }
    }

}
