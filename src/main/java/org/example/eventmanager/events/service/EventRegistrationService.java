package org.example.eventmanager.events.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.eventmanager.events.model.EventStatus;
import org.example.eventmanager.events.model.RegistrationStatus;
import org.example.eventmanager.events.model.UniversalEventMapper;
import org.example.eventmanager.events.model.event.EventDomain;
import org.example.eventmanager.events.model.registration.RegistrationEntity;
import org.example.eventmanager.events.repository.EventRepository;
import org.example.eventmanager.events.repository.RegistrationRepository;
import org.example.eventmanager.security.services.AuthenticationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class EventRegistrationService {

    private final AuthenticationService authenticationService;
    private final EventRepository eventRepository;
    private final RegistrationRepository registrationRepository;
    private final UniversalEventMapper universalEventMapper;


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
}