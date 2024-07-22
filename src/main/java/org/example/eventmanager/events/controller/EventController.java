package org.example.eventmanager.events.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.eventmanager.events.model.UniversalEventMapper;
import org.example.eventmanager.events.model.registration.RegistrationDomain;
import org.example.eventmanager.events.service.EventService;
import org.example.eventmanager.events.model.event.EventDto;
import org.example.eventmanager.events.model.RequestEvent;
import org.example.eventmanager.security.jwt.JwtTokenManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PutMapping("/{eventId}")
    public ResponseEntity<EventDto> updateEvent(
            @PathVariable Long eventId,
            @RequestBody RequestEvent eventToUpdate,
            @RequestHeader("Authorization") String token
    ){
        log.info("Updating event: {}", eventId);
        var validToken = token.substring(7);
        var userLogin = jwtTokenManager.getLoginFromToken(validToken);
        var updatedLocation = eventService.updateEvent(eventId, userLogin, eventToUpdate);
        return ResponseEntity.ok(universalEventMapper.domainToDto(updatedLocation));
    }

    @PostMapping("/search")
    public void searchEvents(){

    }

    @GetMapping("/my")
    public ResponseEntity<List<EventDto>> getMyEvents(
            @RequestHeader("Authorization") String token
    ){
        var validToken = token.substring(7);
        var userLogin = jwtTokenManager.getLoginFromToken(validToken);
        var userEvents = eventService.getUserEvents(userLogin).stream().map(universalEventMapper::domainToDto).toList();
        return ResponseEntity.ok().body(userEvents);
    }

    @PostMapping("/registrations/{eventId}")
    public void registerToEvent(
            @PathVariable Long eventId,
            @RequestHeader("Authorization") String token
    ){
        var validToken = token.substring(7);
        var userLogin = jwtTokenManager.getLoginFromToken(validToken);
        eventService.registerUserToEvent(userLogin, eventId);
    }

    @DeleteMapping("/registrations/cancel/{eventId}")
    public ResponseEntity<?> cancelRegistration(
            @PathVariable Long eventId,
            @RequestHeader("Authorization") String token
    ){
        var validToken = token.substring(7);
        var userLogin = jwtTokenManager.getLoginFromToken(validToken);
        eventService.cancelRegistration(userLogin, eventId);
        return ResponseEntity.status(204).body(null);
    }

    @GetMapping("/registrations/my")
    public ResponseEntity<List<EventDto>> getMyRegistrations(
            @RequestHeader("Authorization") String token
    ){
        var validToken = token.substring(7);
        var userLogin = jwtTokenManager.getLoginFromToken(validToken);
        var events = eventService.getUserRegistrations(userLogin);
        return ResponseEntity.ok().body(events.stream()
                .map(universalEventMapper::domainToDto)
                .toList());
    }
}
