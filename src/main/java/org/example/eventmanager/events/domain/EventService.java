package org.example.eventmanager.events.domain;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.eventmanager.events.UniversalEventMapper;
import org.example.eventmanager.events.api.RequestEvent;
import org.example.eventmanager.events.api.UpdateEvent;
import org.example.eventmanager.events.db.EventEntity;
import org.example.eventmanager.events.api.EventSearchFilter;
import org.example.eventmanager.events.db.EventRepository;
import org.example.eventmanager.events.db.RegistrationRepository;
import org.example.eventmanager.location.domain.LocationService;
import org.example.eventmanager.security.entities.Roles;
import org.example.eventmanager.security.services.AuthenticationService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class EventService {

    private final LocationService locationService;
    private final EventRepository eventRepository;
    private final UniversalEventMapper universalEventMapper;
    private final AuthenticationService authenticationService;
    private final RegistrationRepository registrationRepository;

    public EventDomain createEvent(RequestEvent eventToCreate) {

        var currentUser = authenticationService.getCurrentAuthenticatedUser();
        var locationInfo = locationService.getLocationById(eventToCreate.locationId());
        checkAvailableLocationDate(eventToCreate);
        if (locationInfo.capacity() < eventToCreate.maxPlaces()) {
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

    public EventDomain getEventById(Long eventId) {
        return universalEventMapper.entityToDomain(eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event not found by id: %s".formatted(eventId))));
    }

    public EventDomain deleteEvent(Long eventId) {
        var currentUser = authenticationService.getCurrentAuthenticatedUser();

        var eventToDelete = eventRepository.findById(eventId).orElseThrow(() -> new IllegalArgumentException("Event not found by id: %s".formatted(eventId)));

        if (currentUser.getRole().equals(Roles.ADMIN) || currentUser.getId().equals(eventToDelete.getOwnerId())) {
            if (!eventToDelete.getStatus().equals(EventStatus.WAITING)) {
                throw new IllegalArgumentException("Cannot delete started, finished or closed events");
            }
            eventRepository.changeEventStatus(eventId, EventStatus.CANCELLED);
            registrationRepository.closeAllRegistrations(eventToDelete);
            return universalEventMapper.entityToDomain(eventToDelete);
        } else {
            throw new BadCredentialsException("Данный пользователь не может удалить событие");
        }
    }

    public EventDomain updateEvent(Long eventId, UpdateEvent eventToUpdate) {
        checkCurrentUserCanModifyEvent(eventId);
        checkAvailableLocationDate(eventToUpdate);
        var event = eventRepository.findById(eventId).orElseThrow(() -> new IllegalArgumentException("Event not found by id: %s".formatted(eventId)));
        if (!event.getStatus().equals(EventStatus.WAITING)) {
            throw new IllegalArgumentException("Cannot update event in status: %s".formatted(event.getStatus()));
        }
        if (eventToUpdate.maxPlaces() != null || eventToUpdate.locationId() != null) {
            checkCapacityOfLocation(eventToUpdate.locationId(), eventToUpdate.maxPlaces());
        }

        if (eventToUpdate.maxPlaces() != null && event.getRegistrations().size() > eventToUpdate.maxPlaces()) {
            throw new IllegalArgumentException("Registration count is more than max places");
        }

        Optional.ofNullable(eventToUpdate.date()).ifPresent(event::setDateStart);
        Optional.ofNullable(eventToUpdate.name()).ifPresent(event::setName);
        Optional.ofNullable(eventToUpdate.locationId()).ifPresent(event::setLocationId);
        Optional.ofNullable(eventToUpdate.maxPlaces()).ifPresent(event::setMaxPlaces);
        Optional.ofNullable(eventToUpdate.cost()).ifPresent(event::setCost);
        Optional.ofNullable(eventToUpdate.duration()).ifPresent(event::setDuration);
        Optional.ofNullable(eventToUpdate.date().plusMinutes(eventToUpdate.duration())).ifPresent(event::setDateEnd);

        eventRepository.save(event);
        log.info("Updated event: {}", event);
        System.out.println();
        return universalEventMapper.entityToDomain(event);
    }

    public List<EventDomain> getUserEvents() {
        var currentUser = authenticationService.getCurrentAuthenticatedUser();
        return eventRepository.findAllUserEvents(currentUser.getId()).stream()
                .map(universalEventMapper::entityToDomain)
                .toList();
    }

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

    private void checkCurrentUserCanModifyEvent(Long eventId) {
        var currentUser = authenticationService.getCurrentAuthenticatedUser();
        var event = eventRepository.findById(eventId).orElseThrow(() -> new IllegalArgumentException("Event not found by id: %s".formatted(eventId)));
        if (!event.getOwnerId().equals(currentUser.getId()) && !currentUser.getRole().equals(Roles.ADMIN)) {
            throw new IllegalArgumentException("This user cannot modify this event");
        }
    }

    private void checkAvailableLocationDate(RequestEvent event) {
        var events = eventRepository.findEventByDate(event.date(), event.date().plusMinutes(event.duration()), event.locationId());
        if (!events.isEmpty()) {
            throw new IllegalArgumentException("Location already reserved at this date");
        }
    }

    private void checkAvailableLocationDate(UpdateEvent event) {
        var events = eventRepository.findEventByDate(event.date(), event.date().plusMinutes(event.duration()), event.locationId());
        if(events.size() == 1){
            return;
        }
        if (!events.isEmpty()) {
            throw new IllegalArgumentException("Location already reserved at this date");
        }
    }

    private void checkCapacityOfLocation(Long locationId, Long eventCapacity) {
        var location = locationService.getLocationById(locationId);
        if (eventCapacity > location.capacity()) {
            throw new IllegalArgumentException("Capacity of location is smaller than capacity of event");
        }
    }

}
