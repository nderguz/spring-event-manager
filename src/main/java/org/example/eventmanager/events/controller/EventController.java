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

    @PostMapping
    public ResponseEntity<EventDto> createEvent(
            @RequestBody RequestEvent eventToCreate,
            @RequestHeader("Authorization") String token
    ){
        var validToken = token.substring(7);
        var userLogin = jwtTokenManager.getLoginFromToken(validToken);
        var createdEvent = eventService.createEvent(eventToCreate, userLogin);
        return ResponseEntity.status(201).body(universalEventMapper.domainToDto(createdEvent));
    }

}
