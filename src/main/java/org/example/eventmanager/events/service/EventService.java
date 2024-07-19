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

        //todo проверки на валидность мероприятия
        var userInfo = userService.getUserByLogin(userLogin);
        var locationInfo = locationService.getLocationById(eventToCreate.locationId());
        var eventToSave = universalEventMapper.requestToDomain(userInfo, eventToCreate, locationInfo);
        var savedEvent = eventRepository.save(universalEventMapper.domainToEntity(eventToSave));
        var registration = new RegistrationEntity(null, savedEvent.getId(), userInfo.id());
        registrationRepository.save(registration);
        return universalEventMapper.entityToDomain(savedEvent);
    }

    @Transactional
    public EventDomain getEventById(Long eventId){
        //todo проверка на закрытость события
        return universalEventMapper.entityToDomain(eventRepository.findById(eventId).orElseThrow(() -> new IllegalArgumentException("Event not found by id: %s".formatted(eventId))));
    }

    public EventDomain deleteEvent(Long eventId, String userRole, String userLogin) {
        var userInfo = userService.getUserByLogin(userLogin);
        var eventToDelete = eventRepository.findById(eventId).orElseThrow(() -> new IllegalArgumentException("Event not found by id: %s".formatted(eventId)));

        //todo роль и id совпадает, но не пускает дальше

        if(!userRole.equals(Roles.ADMIN.toString())  || userInfo.id().equals(eventToDelete.getOwnerId())){
            log.info("Roles equals: {}", userRole.equals(Roles.ADMIN.toString()));
            log.info("Ids equals: {}", userInfo.id().equals(eventToDelete.getOwnerId()));
            log.info("User role {}", userRole);
            log.info("User role {}", Roles.USER);
            log.info("User id {}", eventToDelete.getOwnerId());
            log.info("User id {}", userInfo.id());
            throw new BadCredentialsException("Данный пользователь не может удалить событие");
        }

        if (eventToDelete.getStatus().equals(EventStatus.STARTED.toString())){
            throw new IllegalArgumentException("Event is already started");
        }
        if (eventToDelete.getStatus().equals(EventStatus.FINISHED.toString())){
            throw new IllegalArgumentException("Cannot close finished events");
        }
        eventToDelete.setStatus(EventStatus.CANCELLED.toString());
        eventRepository.save(eventToDelete);
        return universalEventMapper.entityToDomain(eventToDelete);
    }

}
