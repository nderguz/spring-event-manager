package org.example.eventmanager.events.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.eventmanager.events.model.*;
import org.example.eventmanager.events.model.event.EventDomain;
import org.example.eventmanager.events.model.event.EventEntity;
import org.example.eventmanager.events.model.event.EventSearchFilter;
import org.example.eventmanager.events.model.registration.RegistrationEntity;
import org.example.eventmanager.events.repository.EventRepository;
import org.example.eventmanager.events.repository.RegistrationRepository;
import org.example.eventmanager.location.LocationService;
import org.example.eventmanager.security.entities.Roles;
import org.example.eventmanager.security.services.AuthenticationService;
import org.example.eventmanager.users.entities.User;
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
    private final RegistrationRepository registrationRepository;
    private final AuthenticationService authenticationService;

    //todo переделать статус регистрации

    @Transactional
    public EventDomain createEvent(RequestEvent eventToCreate) {

        //todo проверки на валидность мероприятия, и также на занятую дату
        var currentUser = authenticationService.getCurrentAuthenticatedUser();
        var locationInfo = locationService.getLocationById(eventToCreate.locationId());
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
                List.of()
        );
        log.info("Saving event {}.", eventEntity);
        var savedEvent = eventRepository.save(eventEntity);
        log.info("Created event with id: '{}'", savedEvent.getId());
        return universalEventMapper.entityToDomain(savedEvent);
    }

    @Transactional
    public EventDomain getEventById(Long eventId) {
        return universalEventMapper.entityToDomain(eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found by id: %s".formatted(eventId))));
    }

    //todo заменить статус записи удаленных событий либо дропать записи на событе
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
            return universalEventMapper.entityToDomain(eventToDelete);
        } else {
            throw new BadCredentialsException("Данный пользователь не может удалить событие");
        }
    }

    @Transactional
    public EventDomain updateEvent(Long eventId, RequestEvent eventToUpdate) {
        //todo проверки на валидность события
        var currentUser = authenticationService.getCurrentAuthenticatedUser();
        checkCurrentUserCanModifyEvent(currentUser.getId());
        var event = eventRepository.findById(eventId).orElseThrow(() -> new IllegalArgumentException("Event not found by id: %s".formatted(eventId)));
        if (currentUser.getRole().toString().equals(Roles.ADMIN.toString()) || currentUser.getId().equals(event.getOwnerId())) {
            event.setDate(eventToUpdate.date());
            event.setCost(eventToUpdate.cost());
            event.setDuration(eventToUpdate.duration());
            event.setMaxPlaces(eventToUpdate.maxPlaces());
            event.setLocationId(eventToUpdate.locationId());
            event.setName(eventToUpdate.name());
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
    public void registerUserToEvent(Long eventId) throws NullPointerException {
        //todo проверка на заполненность мероприятий
        var currentUser = authenticationService.getCurrentAuthenticatedUser();
        var event = eventRepository.findById(eventId).orElseThrow(() -> new IllegalArgumentException("Event not found by id: %s".formatted(eventId)));
        if(event.getOwnerId().equals(currentUser.getId())){
            throw new IllegalArgumentException("Owner cannot register to the event");
        }
        if (event.getStatus().equals(EventStatus.FINISHED) || event.getStatus().equals(EventStatus.CANCELLED) || event.getStatus().equals(EventStatus.STARTED)) {
            throw new IllegalArgumentException("User cannot register to finished, cancelled or already started event");
        }
        var registration = registrationRepository.findUserRegistration(currentUser.getId(), event.getId());
        if(registration.isPresent()){
            throw new IllegalArgumentException("User already registered to this event");
        }
        registrationRepository.save(
                new RegistrationEntity(
                        null,
                        eventRepository.findById(event.getId()).orElseThrow(),
                        currentUser.getId(),
                        RegistrationStatus.OPENED
                )
        );
    }

    @Transactional
    public void cancelRegistration(Long eventId) {
        var currentUser = authenticationService.getCurrentAuthenticatedUser();
        var event = eventRepository.findById(eventId).orElseThrow(() -> new IllegalArgumentException("Event not found by id: %s".formatted(eventId)));
        if (event.getStatus().equals(EventStatus.STARTED) || event.getStatus().equals(EventStatus.FINISHED)) {
            throw new IllegalArgumentException("Cannot cancel registration at started or finished event");
        }
        var registration = registrationRepository.findUserRegistration(currentUser.getId(), event.getId());
        if(registration.isEmpty()){
            throw new IllegalArgumentException("Registration not found");
        }
        registrationRepository.closeRegistration(currentUser.getId(), event);
    }

    @Transactional
    public List<EventDomain> getUserRegistrations() {
        var currentUser = authenticationService.getCurrentAuthenticatedUser();
        var foundEvents = registrationRepository.findUserEvents(currentUser.getId());
        return foundEvents.stream().map(universalEventMapper::entityToDomain).toList();
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

    private void checkCurrentUserCanModifyEvent(Long eventId){
        var currentUser = authenticationService.getCurrentAuthenticatedUser();
        var event = getEventById(eventId);

        if(!event.ownerId().equals(currentUser.getId()) && !currentUser.getRole().equals(Roles.ADMIN)){
            throw new IllegalArgumentException("This user cannot modify this event");
        }
    }

    private void checkCapacityOfLocation(Long eventId){

    }
}
