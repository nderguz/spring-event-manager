package org.example.eventmanager.events.api;

import org.example.eventmanager.events.domain.EventStatus;

import java.time.LocalDateTime;

public record EventDto(
        Integer occupiedPlaces,
        LocalDateTime date,
        Integer duration,
        Integer cost,
        Integer maxPlaces,
        Long locationId,
        String name,
        Long id,
        Long ownerId,
        EventStatus status
) {
}
