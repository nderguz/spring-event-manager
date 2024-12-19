package org.example.eventmanager.events.domain;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.eventmanager.events.UniversalEventMapper;
import org.example.eventmanager.events.api.model.EventFullInfo;
import org.example.eventmanager.events.db.model.RegistrationEntity;
import org.example.eventmanager.events.db.EventRepository;
import org.example.eventmanager.events.db.RegistrationRepository;
import org.example.eventmanager.events.domain.model.EventStatus;
import org.example.eventmanager.events.domain.model.RegistrationStatus;
import org.example.eventmanager.security.services.AuthenticationService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class EventRegistrationService {

    private final AuthenticationService authenticationService;
    private final EventRepository eventRepository;
    private final RegistrationRepository registrationRepository;
    private final UniversalEventMapper universalEventMapper;

    public void registerUserToEvent(Long eventId) {
        var currentUser = authenticationService.getCurrentAuthenticatedUser();
        var event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found by id: %s".formatted(eventId)));
        checkCapacityOfEvent(event.getId());
        if(event.getOwner().getId().equals(currentUser.getId())){
            throw new IllegalArgumentException("Owner cannot register to the event");
        }
        if (event.getStatus().equals(EventStatus.FINISHED) || event.getStatus().equals(EventStatus.CANCELLED)) {
            throw new IllegalArgumentException("User cannot register to finished or cancelled event");
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

    public List<EventFullInfo> getUserRegistrations() {
        var currentUser = authenticationService.getCurrentAuthenticatedUser();
        var foundEvents = registrationRepository.findUserEvents(currentUser.getId()).stream()
                .map(universalEventMapper::entityToDomain).toList();
        return foundEvents.stream().map(universalEventMapper::domainToDto).toList();
    }

    private void checkCapacityOfEvent(Long eventId){
        var registrations = eventRepository.getEventOpenedRegistrations(eventId);
        var event = eventRepository.findById(eventId).orElseThrow(() -> new IllegalArgumentException("Event not found by id: %s".formatted(eventId)));
        if (registrations.size() + 1 > event.getMaxPlaces() ){
            throw new IllegalArgumentException("The allowed guest limit for the event has been exceeded");
        }
    }
}
