package org.example.eventmanager.events.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.eventmanager.events.model.UniversalEventMapper;
import org.example.eventmanager.events.model.event.EventSearchFilter;
import org.example.eventmanager.events.service.EventService;
import org.example.eventmanager.events.model.event.EventDto;
import org.example.eventmanager.events.model.RequestEvent;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
@AllArgsConstructor
@Slf4j
public class EventController {

    private final EventService eventService;
    private final UniversalEventMapper universalEventMapper;

    @PostMapping
    public ResponseEntity<EventDto> createEvent(
            @RequestBody RequestEvent eventToCreate
    ) {
        var createdEvent = eventService.createEvent(eventToCreate);
        return ResponseEntity.status(201).body(universalEventMapper.domainToDto(createdEvent));
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<EventDto> deleteEvent(
            @PathVariable Long eventId
    ) {

        var eventToDelete = eventService.deleteEvent(eventId);
        return ResponseEntity.status(204).body(universalEventMapper.domainToDto(eventToDelete));
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventDto> getEventById(
            @PathVariable Long eventId
    ) {
        var foundLocation = eventService.getEventById(eventId);
        return ResponseEntity.ok(universalEventMapper.domainToDto(foundLocation));
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<EventDto> updateEvent(
            @PathVariable Long eventId,
            @RequestBody RequestEvent eventToUpdate
    ) {
        var updatedLocation = eventService.updateEvent(eventId, eventToUpdate);
        return ResponseEntity.ok(universalEventMapper.domainToDto(updatedLocation));
    }

    @PostMapping("/search")
    public ResponseEntity<List<EventDto>> searchEvents(
            @RequestBody @Valid EventSearchFilter searchFilter
    ) {
        log.info("Get request for search events: filter={}", searchFilter);
        var foundEvents = eventService.searchByFilter(searchFilter);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(foundEvents.stream()
                        .map(universalEventMapper::domainToDto)
                        .toList()
                );
    }

    @GetMapping("/my")
    public ResponseEntity<List<EventDto>> getMyEvents() {
        var userEvents = eventService.getUserEvents().stream().map(universalEventMapper::domainToDto).toList();
        return ResponseEntity.ok().body(userEvents);
    }
}
