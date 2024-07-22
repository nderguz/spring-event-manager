package org.example.eventmanager.events.controller;

import lombok.AllArgsConstructor;
import org.example.eventmanager.events.model.UniversalEventMapper;
import org.example.eventmanager.events.model.event.EventDto;
import org.example.eventmanager.events.service.EventRegistrationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/events/registrations")
@AllArgsConstructor
public class EventRegistrationController {

    private final UniversalEventMapper universalEventMapper;
    private final EventRegistrationService eventRegistrationService;

    @PostMapping("/{eventId}")
    public void registerToEvent(
            @PathVariable Long eventId
    ) {
        eventRegistrationService.registerUserToEvent(eventId);
    }

    @DeleteMapping("/cancel/{eventId}")
    public ResponseEntity<?> cancelRegistration(
            @PathVariable Long eventId
    ) {
        eventRegistrationService.cancelRegistration(eventId);
        return ResponseEntity.status(204).body(null);
    }

    @GetMapping("/my")
    public ResponseEntity<List<EventDto>> getMyRegistrations(
    ) {
        var events = eventRegistrationService.getUserRegistrations();
        return ResponseEntity.ok().body(events.stream()
                .map(universalEventMapper::domainToDto)
                .toList());
    }

    @GetMapping("/debug-event-count")
    public void testRequest(){
        eventRegistrationService.checkCapacityOfEvent(1L);
    }
}
