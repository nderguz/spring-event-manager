package org.example.eventmanager.events.domain;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.eventmanager.events.UniversalEventMapper;
import org.example.eventmanager.events.api.EventDto;
import org.example.eventmanager.events.api.EventSearchFilter;
import org.example.eventmanager.events.api.RequestEvent;
import org.example.eventmanager.events.api.UpdateEvent;
import org.example.eventmanager.events.db.EventEntity;
import org.example.eventmanager.events.db.EventRepository;
import org.example.eventmanager.events.db.RegistrationRepository;
import org.example.eventmanager.location.UniversalLocationMapper;
import org.example.eventmanager.location.domain.Location;
import org.example.eventmanager.location.domain.LocationService;
import org.example.eventmanager.security.entities.Roles;
import org.example.eventmanager.security.services.AuthenticationService;
import org.example.eventmanager.users.UniversalUserMapper;
import org.example.eventmanager.users.domain.User;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventService {

    private final LocationService locationService;
    private final EventRepository eventRepository;
    private final UniversalEventMapper universalEventMapper;
    private final AuthenticationService authenticationService;
    private final RegistrationRepository registrationRepository;
    private final UniversalLocationMapper universalLocationMapper;
    private final UniversalUserMapper universalUserMapper;

    @Transactional
    public EventDto createEvent(RequestEvent eventToCreate) {
        checkAvailableLocationDate(eventToCreate);
        var currentUser = authenticationService.getCurrentAuthenticatedUser();
        var locationInfo = locationService.getLocationById(eventToCreate.locationId());
        if (locationInfo.capacity() < eventToCreate.maxPlaces()) {
            throw new IllegalArgumentException("Capacity of location is: %s, but maxPlaces is: %s".formatted(locationInfo.capacity(), eventToCreate.maxPlaces()));
        }
        var savedEvent = eventRepository.save(buildNewEventEntity(eventToCreate, currentUser, locationInfo));
        return buildNewEventDto(savedEvent);
    }

    @Transactional(readOnly = true)
    public EventDto getEventById(Long eventId) {
        var foundEvent = universalEventMapper.entityToDomain(eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event not found by id: %s".formatted(eventId))));
        return universalEventMapper.domainToDto(foundEvent);
    }


    @Transactional
    public void deleteEvent(Long eventId) {
        var currentUser = authenticationService.getCurrentAuthenticatedUser();
        var toDeleteCandidate = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found by id: %s".formatted(eventId)));
        if (currentUser.getRole().equals(Roles.ADMIN) || currentUser.getId().equals(toDeleteCandidate.getOwner().getId())) {

            log.warn("Attempt to delete an event that does not belong to an administrator by user with id =%s.".formatted(currentUser.getId()));

            if (!toDeleteCandidate.getStatus().equals(EventStatus.WAITING)) {
                throw new IllegalArgumentException("Cannot delete started, finished or closed events");
            }
            eventRepository.changeEventStatus(eventId, EventStatus.CANCELLED);
            registrationRepository.closeAllRegistrations(toDeleteCandidate);
        } else {
            throw new BadCredentialsException("Данный пользователь не может удалить событие");
        }
    }

    @Transactional
    public EventDto updateEvent(Long eventId, UpdateEvent eventToUpdate) {
        checkCurrentUserCanModifyEvent(eventId);
        checkAvailableLocationDate(eventToUpdate);
        var event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found by id: %s".formatted(eventId)));
        checkEventValidation(event, eventToUpdate);
        event.builder()
                .dateStart(eventToUpdate.date())
                .name(event.getName())
                .location(universalLocationMapper.domainToEntity(locationService.getLocationById(eventToUpdate.locationId())))
                .maxPlaces(eventToUpdate.maxPlaces())
                .cost(eventToUpdate.cost())
                .duration(eventToUpdate.duration())
                .dateEnd(eventToUpdate.date().plusMinutes(eventToUpdate.duration()))
                .build();

        eventRepository.save(event);
        return universalEventMapper.domainToDto(universalEventMapper.entityToDomain(event));
    }

    @Transactional
    public List<EventDto> getUserEvents() {
        var currentUser = authenticationService.getCurrentAuthenticatedUser();
        return eventRepository.findAllUserEvents(currentUser.getId()).stream()
                .map(universalEventMapper::entityToDomain)
                .map(universalEventMapper::domainToDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<EventDto> searchByFilter(EventSearchFilter searchFilter) {
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
        ).stream().map(universalEventMapper::entityToDomain).toList();
        return foundEntities.stream()
                .map(universalEventMapper::domainToDto)
                .toList();
    }

    private EventDto buildNewEventDto(EventEntity eventEntity) {
        return EventDto.builder()
                .occupiedPlaces(0)
                .date(eventEntity.getDateStart())
                .duration(eventEntity.getDuration())
                .cost(eventEntity.getCost())
                .maxPlaces(eventEntity.getMaxPlaces())
                .locationId(eventEntity.getLocation().getId())
                .name(eventEntity.getName())
                .id(eventEntity.getId())
                .ownerId(eventEntity.getId())
                .status(eventEntity.getStatus())
                .build();
    }

    private void checkEventValidation(EventEntity eventEntity, UpdateEvent updateEvent) {
        if (!eventEntity.getStatus().equals(EventStatus.WAITING)) {
            throw new IllegalArgumentException("Cannot update event in status: %s".formatted(eventEntity.getStatus()));
        }
        if (updateEvent.maxPlaces() != null || updateEvent.locationId() != null) {
            checkCapacityOfLocation(updateEvent.locationId(), updateEvent.maxPlaces());
        }

        if (updateEvent.maxPlaces() != null && eventEntity.getRegistrations().size() > updateEvent.maxPlaces()) {
            throw new IllegalArgumentException("Registration count is more than max places");
        }
    }

    private EventEntity buildNewEventEntity(RequestEvent eventToCreate, User user, Location location) {
        return EventEntity.builder()
                .location(universalLocationMapper.domainToEntity(location))
                .name(eventToCreate.name())
                .status(EventStatus.WAITING)
                .owner(universalUserMapper.domainToEntity(user))
                .maxPlaces(eventToCreate.maxPlaces())
                .cost(eventToCreate.cost())
                .duration(eventToCreate.duration())
                .dateStart(eventToCreate.date())
                .dateEnd(eventToCreate.date().plusMinutes(eventToCreate.duration()))
                .build();
    }

    private void checkCurrentUserCanModifyEvent(Long eventId) {
        var currentUser = authenticationService.getCurrentAuthenticatedUser();
        var event = eventRepository.findById(eventId).orElseThrow(() -> new IllegalArgumentException("Event not found by id: %s".formatted(eventId)));
        if (!event.getOwner().getId().equals(currentUser.getId()) && !currentUser.getRole().equals(Roles.ADMIN)) {
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
        if (events.size() == 1) {
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
