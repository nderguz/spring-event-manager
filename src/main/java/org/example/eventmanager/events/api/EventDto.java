package org.example.eventmanager.events.api;

import lombok.Builder;
import org.example.eventmanager.events.domain.EventStatus;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Builder
public record EventDto(
        Integer occupiedPlaces,
        ZonedDateTime date,
        Integer duration,
        BigDecimal cost,
        Long maxPlaces,
        Long locationId,
        String name,
        Long id,
        Long ownerId,
        EventStatus status
) {
}
