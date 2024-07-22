package org.example.eventmanager.events.model.event;

import org.example.eventmanager.events.model.EventStatus;

import java.util.Date;

public record EventDomain (
        Integer occupiedPlaces,
        Date date,
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
