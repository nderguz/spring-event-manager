package org.example.eventmanager.events.domain.model;

public record Registration(
        Long id,
        Long userId,
        Long eventId
) {
}
