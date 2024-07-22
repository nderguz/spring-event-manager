package org.example.eventmanager.events.model.event;

import org.example.eventmanager.events.model.EventStatus;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

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
