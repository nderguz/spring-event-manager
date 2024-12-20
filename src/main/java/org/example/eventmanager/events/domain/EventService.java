package org.example.eventmanager.events.domain;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.eventmanager.events.api.model.EventFullInfo;
import org.example.eventmanager.events.api.model.EventRequestToCreate;
import org.example.eventmanager.events.api.model.EventSearchFilter;
import org.example.eventmanager.events.api.model.EventUpdateRequest;
import org.example.eventmanager.events.db.EventRepository;
import org.example.eventmanager.events.db.RegistrationRepository;
import org.example.eventmanager.events.db.model.EventEntity;
import org.example.eventmanager.events.domain.model.EventStatus;
import org.example.eventmanager.location.UniversalLocationMapper;
import org.example.eventmanager.location.api.Location;
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
    private final AuthenticationService authenticationService;
    private final RegistrationRepository registrationRepository;
    private final UniversalLocationMapper universalLocationMapper;
    private final UniversalUserMapper universalUserMapper;

    @Transactional
    public EventFullInfo createEvent(EventRequestToCreate eventToCreate) {
        checkLocationDateAvailability(eventToCreate);
        var getValidatedLocation = getValidatedLocation(eventToCreate.locationId(), eventToCreate.maxPlaces());
        var currentUser = authenticationService.getCurrentAuthenticatedUser();
        var savedEvent = eventRepository.save(
                buildNewEventEntity(eventToCreate,
                        currentUser,
                        getValidatedLocation));
        return buildEventResponse(savedEvent);
    }

    @Transactional(readOnly = true)
    public EventFullInfo getEventById(Long eventId) {
        var foundEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event not found by id: %s".formatted(eventId)));
        return buildEventResponse(foundEvent);
    }

    @Transactional
    public void closeEvent(Long eventId) {
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
            throw new BadCredentialsException("Current user cannot close this event");
        }
    }

    @Transactional
    public EventFullInfo updateEvent(Long eventId, EventUpdateRequest eventToUpdate) {
        checkLocationDateAvailability(eventToUpdate);
        var event = checkCurrentUserCanModifyEvent(eventId);
        //todo зарефакторить изменение сущности hibernate
        checkEventValidation(event, eventToUpdate);
        event.setDateStart(eventToUpdate.date());
        event.setName(eventToUpdate.name());
        event.setLocation(universalLocationMapper.buildEntity(locationService.getLocationById(eventToUpdate.locationId())));
        event.setMaxPlaces(eventToUpdate.maxPlaces());
        event.setCost(eventToUpdate.cost());
        event.setDuration(eventToUpdate.duration());
        event.setDateEnd(eventToUpdate.date().plusMinutes(eventToUpdate.duration()));

        eventRepository.save(event);
        return buildEventResponse(event);
    }

    @Transactional
    public List<EventFullInfo> getUserEvents() {
        var currentUser = authenticationService.getCurrentAuthenticatedUser();
        return eventRepository.findAllUserEvents(currentUser.getId()).stream()
                .map(this::buildEventResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<EventFullInfo> searchByFilter(EventSearchFilter searchFilter) {
        return eventRepository.findEvents(
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
                ).stream()
                .map(this::buildEventResponse)
                .toList();
    }

    private EventFullInfo buildEventResponse(EventEntity eventEntity) {
        return EventFullInfo.builder()
                //todo решить проблему n+1 при запросе
                .occupiedPlaces(registrationRepository.countRegistrations(eventEntity.getId()))
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

    private void checkEventValidation(EventEntity eventEntity, EventUpdateRequest updateEvent) {
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

    private EventEntity buildNewEventEntity(EventRequestToCreate eventToCreate, User user, Location location) {
        return EventEntity.builder()
                .location(universalLocationMapper.buildEntity(location))
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

    private EventEntity checkCurrentUserCanModifyEvent(Long eventId) {
        var currentUser = authenticationService.getCurrentAuthenticatedUser();
        var event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found by id: %s".formatted(eventId)));
        if (!event.getOwner().getId().equals(currentUser.getId()) && !currentUser.getRole().equals(Roles.ADMIN)) {
            throw new IllegalArgumentException("This user cannot modify this event");
        }
        return event;
    }

    private void checkLocationDateAvailability(EventRequestToCreate event) {
        var events = eventRepository.checkIfLocationDateReserved(event.date(), event.date().plusMinutes(event.duration()), event.locationId(), EventStatus.CANCELLED);
        if (events != 0) {
            throw new IllegalArgumentException("Location already reserved at this date");
        }
    }

    private void checkLocationDateAvailability(EventUpdateRequest event) {
        var events = eventRepository.checkIfLocationDateReserved(event.date(), event.date().plusMinutes(event.duration()), event.locationId(), EventStatus.CANCELLED);
        //todo проверить, что это именно наше событие
        if (events != 1) {
            throw new IllegalArgumentException("Location already reserved at this date");
        }
    }

    private void checkCapacityOfLocation(Long locationId, Long eventCapacity) {
        var location = locationService.getLocationById(locationId);
        if (eventCapacity > location.capacity()) {
            throw new IllegalArgumentException("Capacity of location is smaller than capacity of event");
        }
    }

    private Location getValidatedLocation(Long locationId, Long requiredPlaces) {

        var locationInfo = locationService.getLocationById(locationId);
        if (locationInfo.capacity() < requiredPlaces) {
            throw new IllegalArgumentException("Capacity of location is: %s, but maxPlaces is: %s".formatted(locationInfo.capacity(), requiredPlaces));
        }
        return locationInfo;
    }
}
