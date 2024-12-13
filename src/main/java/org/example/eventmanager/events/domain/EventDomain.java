package org.example.eventmanager.events.domain;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

@Builder
public record EventDomain(
        List<Registration> occupiedPlaces,
        LocalDateTime date,
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
