package org.example.eventmanager.events.api;

import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.eventmanager.events.api.model.EventFullInfo;
import org.example.eventmanager.events.api.model.EventRequestToCreate;
import org.example.eventmanager.events.api.model.EventSearchFilter;
import org.example.eventmanager.events.api.model.EventUpdateRequest;
import org.example.eventmanager.events.domain.EventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EventFullInfo> createEvent(
            @RequestBody @Valid @NonNull EventRequestToCreate eventToCreate
    ) {
        var createdEvent = eventService.createEvent(eventToCreate);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEvent);
    }

    @DeleteMapping(value = "/{eventId}")
    public ResponseEntity<Void> deleteEvent(
            @PathVariable Long eventId
    ) {
        eventService.deleteEvent(eventId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/{eventId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<EventFullInfo> getEventById(
            @PathVariable Long eventId
    ) {
        return ResponseEntity.ok(eventService.getEventById(eventId));
    }

    @PutMapping(value ="/{eventId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<EventFullInfo> updateEvent(
            @PathVariable Long eventId,
            @RequestBody EventUpdateRequest eventToUpdate
    ) {
        return ResponseEntity.ok(eventService.updateEvent(eventId, eventToUpdate));
    }

    @PostMapping(value = "/search",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<EventFullInfo>> searchEvents(
            @RequestBody @Valid EventSearchFilter searchFilter
    ) {
        log.info("Get request for search events: filter={}", searchFilter);
        return ResponseEntity.status(HttpStatus.OK).body(eventService.searchByFilter(searchFilter));
    }

    @GetMapping(value = "/my",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<EventFullInfo>> getMyEvents() {
        return ResponseEntity.status(HttpStatus.OK).body(eventService.getUserEvents());
    }
}
