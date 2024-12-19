package org.example.eventmanager.events.api.model;

import lombok.Builder;
import org.example.eventmanager.events.domain.model.EventStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record EventFullInfo(
        Integer occupiedPlaces,
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
