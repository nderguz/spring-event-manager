package org.example.eventmanager.events.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.eventmanager.events.model.EventDomain;
import org.example.eventmanager.events.model.RequestEvent;
import org.example.eventmanager.events.model.UniversalEventMapper;
import org.example.eventmanager.events.repository.EventRepository;
import org.example.eventmanager.location.LocationService;
import org.example.eventmanager.users.services.UserService;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class EventService {

    private final UserService userService;
    private final LocationService locationService;
    private final EventRepository eventRepository;
    private final UniversalEventMapper universalEventMapper;

    @Transactional
    public EventDomain createEvent(RequestEvent eventToCreate, String userLogin) {
        var userInfo = userService.getUserByLogin(userLogin);
        var locationInfo = locationService.getLocationById(eventToCreate.locationId());
        var eventToSave = universalEventMapper.requestToDomain(userInfo, eventToCreate, locationInfo);
        log.info("Creating event: {}", eventToSave);
        var savedEvent = eventRepository.save(universalEventMapper.domainToEntity(eventToSave));
        return universalEventMapper.entityToDomain(savedEvent);
    }
}
