package org.example.eventmanager.events.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.eventmanager.events.model.UniversalEventMapper;
import org.example.eventmanager.events.service.EventService;
import org.example.eventmanager.events.model.EventDto;
import org.example.eventmanager.events.model.RequestEvent;
import org.example.eventmanager.security.jwt.JwtTokenManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/events")
@AllArgsConstructor
@Slf4j
public class EventController {

    private final EventService eventService;
    private final JwtTokenManager jwtTokenManager;
    private final UniversalEventMapper universalEventMapper;

    //todo вынести проверку пользователя в отдельный метод

    @PostMapping
    public ResponseEntity<EventDto> createEvent(
            @RequestBody RequestEvent eventToCreate,
            @RequestHeader("Authorization") String token
    ){
        log.info("Creating event: {}", eventToCreate);
        var validToken = token.substring(7);
        var userLogin = jwtTokenManager.getLoginFromToken(validToken);
        var createdEvent = eventService.createEvent(eventToCreate, userLogin);
        return ResponseEntity.status(201).body(universalEventMapper.domainToDto(createdEvent));
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<EventDto> deleteEvent(
            @PathVariable Long eventId,
            @RequestHeader("Authorization") String token
    ){
        log.info("Deleting event: {}", eventId);
        var validToken = token.substring(7);
        var userLogin = jwtTokenManager.getLoginFromToken(validToken);
        var userRole = jwtTokenManager.getRoleFromToken(validToken);
        var eventToDelete = eventService.deleteEvent(eventId, userRole, userLogin);
        return ResponseEntity.status(204).body(universalEventMapper.domainToDto(eventToDelete));
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventDto> getEventById(
            @PathVariable Long eventId
    ){
        var foundLocation = eventService.getEventById(eventId);
        return ResponseEntity.ok(universalEventMapper.domainToDto(foundLocation));
    }

}
