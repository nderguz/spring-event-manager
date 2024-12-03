package org.example.eventmanager.events.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.eventmanager.events.domain.EventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
@Slf4j
public class EventController {

    private final EventService eventService;

    @PostMapping
    public ResponseEntity<EventDto> createEvent(
            @RequestBody
            @Valid
            RequestEvent eventToCreate
    ) {
        var createdEvent = eventService.createEvent(eventToCreate);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEvent);
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteEvent(
            @PathVariable Long eventId
    ) {
        eventService.deleteEvent(eventId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventDto> getEventById(
            @PathVariable Long eventId
    ) {
        return ResponseEntity.ok(eventService.getEventById(eventId));
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<EventDto> updateEvent(
            @PathVariable Long eventId,
            @RequestBody UpdateEvent eventToUpdate
    ) {
        return ResponseEntity.ok(eventService.updateEvent(eventId, eventToUpdate));
    }

    @PostMapping("/search")
    public ResponseEntity<List<EventDto>> searchEvents(
            @RequestBody @Valid EventSearchFilter searchFilter
    ) {
        log.info("Get request for search events: filter={}", searchFilter);
        return ResponseEntity.status(HttpStatus.OK).body(eventService.searchByFilter(searchFilter));
    }

    @GetMapping("/my")
    public ResponseEntity<List<EventDto>> getMyEvents() {
        return ResponseEntity.status(HttpStatus.OK).body(eventService.getUserEvents());
    }
}
