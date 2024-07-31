package org.example.eventmanager.events.domain;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

public record EventDomain (
        List<Registration> occupiedPlaces,
        ZonedDateTime date,
        Integer duration,
        BigDecimal cost,
        Integer maxPlaces,
        Long locationId,
        String name,
        Long id,
        Long ownerId,
        EventStatus status
){
}
