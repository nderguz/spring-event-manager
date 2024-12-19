package org.example.eventmanager.events.api;

import lombok.RequiredArgsConstructor;
import org.example.eventmanager.events.api.model.EventFullInfo;
import org.example.eventmanager.events.domain.EventRegistrationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/events/registrations")
@RequiredArgsConstructor
public class EventRegistrationController {

    private final EventRegistrationService eventRegistrationService;

    @PostMapping("/{eventId}")
    public ResponseEntity<Void> registerToEvent(
            @PathVariable Long eventId
    ) {
        eventRegistrationService.registerUserToEvent(eventId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/cancel/{eventId}")
    public ResponseEntity<Void> cancelRegistration(
            @PathVariable Long eventId
    ) {
        eventRegistrationService.cancelRegistration(eventId);
        return ResponseEntity.status(204).build();
    }

    @GetMapping("/my")
    public ResponseEntity<List<EventFullInfo>> getMyRegistrations(
    ) {
        return ResponseEntity.ok().body(eventRegistrationService.getUserRegistrations());
    }

}
