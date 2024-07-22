package org.example.eventmanager.events.model.event;

import org.example.eventmanager.events.model.EventStatus;
import java.time.LocalDateTime;

public record EventSearchFilter (
        String name,
        Integer placesMin,
        Integer placesMax,
        LocalDateTime dateStartAfter,
        LocalDateTime dateStartBefore,
        Long costMin,
        Long costMax,
        Integer durationMin,
        Integer durationMax,
        Long locationId,
        EventStatus eventStatus
){
}
