package org.example.eventmanager.events.api;

import org.example.eventmanager.events.domain.EventStatus;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public record EventDto(
        Integer occupiedPlaces,
        ZonedDateTime date,
        Integer duration,
        BigDecimal cost,
        Integer maxPlaces,
        Long locationId,
        String name,
        Long id,
        Long ownerId,
        EventStatus status
) {
}
