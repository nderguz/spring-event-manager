package org.example.eventmanager.events.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.eventmanager.events.model.*;
import org.example.eventmanager.events.repository.EventRepository;
import org.example.eventmanager.events.repository.RegistrationRepository;
import org.example.eventmanager.location.LocationService;
import org.example.eventmanager.security.entities.Roles;
import org.example.eventmanager.users.services.UserService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
@Slf4j
public class EventService {

    private final UserService userService;
    private final LocationService locationService;
    private final EventRepository eventRepository;
    private final UniversalEventMapper universalEventMapper;
    private final RegistrationRepository registrationRepository;

    @Transactional
    public EventDomain createEvent(RequestEvent eventToCreate, String userLogin) {

        //todo проверки на валидность мероприятия, и также на занятую дату

        var userInfo = userService.getUserByLogin(userLogin);
        var locationInfo = locationService.getLocationById(eventToCreate.locationId());
        var eventToSave = universalEventMapper.requestToDomain(userInfo, eventToCreate, locationInfo);
        var savedEvent = eventRepository.save(universalEventMapper.domainToEntity(eventToSave));
        var registration = new RegistrationEntity(null, savedEvent.getId(), userInfo.getId(), RegistrationStatus.OPENED.name());
        registrationRepository.save(registration);
        return universalEventMapper.entityToDomain(savedEvent);
    }

    @Transactional
    public EventDomain getEventById(Long eventId) {
        return universalEventMapper.entityToDomain(eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found by id: %s".formatted(eventId))));
    }

    @Transactional
    public EventDomain deleteEvent(Long eventId, String userRole, String userLogin) {

        var userInfo = userService.getUserByLogin(userLogin);

        var eventToDelete = eventRepository.findById(eventId).orElseThrow(() -> new IllegalArgumentException("Event not found by id: %s".formatted(eventId)));

        if (userRole.equals(Roles.ADMIN.toString()) || userInfo.getId().equals(eventToDelete.getOwnerId())) {
            if (eventToDelete.getStatus().equals(EventStatus.STARTED.toString())) {
                throw new IllegalArgumentException("Event is already started");
            }
            if (eventToDelete.getStatus().equals(EventStatus.FINISHED.toString())) {
                throw new IllegalArgumentException("Cannot close finished events");
            }
            eventToDelete.setStatus(EventStatus.CANCELLED.toString());
            eventRepository.save(eventToDelete);
            return universalEventMapper.entityToDomain(eventToDelete);
        } else {
            throw new BadCredentialsException("Данный пользователь не может удалить событие");
        }

    }

    @Transactional
    public EventDomain updateEvent(Long eventId, String userLogin, RequestEvent eventToUpdate) {

        var userInfo = userService.getUserByLogin(userLogin);
        var event = eventRepository.findById(eventId).orElseThrow(() -> new IllegalArgumentException("Event not found by id: %s".formatted(eventId)));

        if (userInfo.getRole().toString().equals(Roles.ADMIN.toString()) || userInfo.getId().equals(event.getOwnerId())) {
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
    public List<EventDomain> getUserEvents(String userLogin) {
        var userInfo = userService.getUserByLogin(userLogin);
        return eventRepository.findAllUserEvents(userInfo.getId()).stream()
                .map(universalEventMapper::entityToDomain)
                .toList();
    }

    @Transactional
    public void registerUserToEvent(String userLogin, Long eventId) throws NullPointerException {
        //todo проверка на заполненность мероприятий


        var event = eventRepository.findById(eventId).orElseThrow(() -> new IllegalArgumentException("Event not found by id: %s".formatted(eventId)));
        if (event.getStatus().equals(EventStatus.FINISHED.toString()) || event.getStatus().equals(EventStatus.CANCELLED.toString())) {
            throw new IllegalArgumentException("Cannot register user to finished or cancelled event");
        }
        var user = userService.getUserByLogin(userLogin);
        var possibleRegistration = registrationRepository.findUserRegistration(user.getId(), event.getId());

        //todo проверка на перезаписть закрытой записи
        if (possibleRegistration != null && Objects.equals(possibleRegistration.getRegistrationStatus(), RegistrationStatus.OPENED.name())) {
            throw new IllegalArgumentException("User already registered at this event");
        }

        var registration = new RegistrationEntity(null, event.getId(), user.getId(), RegistrationStatus.OPENED.name());
        registrationRepository.save(registration);
    }

    @Transactional
    public void cancelRegistration(String userLogin, Long eventId) {
        //todo сделать правильный ответ
        var user = userService.getUserByLogin(userLogin);
        var event = eventRepository.findById(eventId).orElseThrow(() -> new IllegalArgumentException("Event not found by id: %s".formatted(eventId)));
        if (event.getStatus().equals(EventStatus.STARTED.name()) || event.getStatus().equals(EventStatus.FINISHED.name())) {
            throw new IllegalArgumentException("Cannot cancel registration at started or finished event");
        }
        var registration = registrationRepository.findUserRegistration(user.getId(), event.getId());
        registration.setRegistrationStatus(RegistrationStatus.CLOSED.name());
        registrationRepository.save(registration);
    }
}
