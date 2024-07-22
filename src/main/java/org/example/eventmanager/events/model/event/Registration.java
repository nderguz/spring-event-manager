package org.example.eventmanager.events.model.event;

public record Registration(
        Long id,
        Long userId,
        Long eventId
) {
}
