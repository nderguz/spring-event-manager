package org.example.eventmanager.events.domain;

public record Registration(
        Long id,
        Long userId,
        Long eventId
) {
}
