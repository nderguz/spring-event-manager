package org.example.eventmanager.events.domain;

import java.time.LocalDateTime;
import java.util.List;

public record EventDomain (
        List<Registration> occupiedPlaces,
        LocalDateTime date,
        Integer duration,
        Integer cost,
        Integer maxPlaces,
        Long locationId,
        String name,
        Long id,
        Long ownerId,
        EventStatus status
){
}
