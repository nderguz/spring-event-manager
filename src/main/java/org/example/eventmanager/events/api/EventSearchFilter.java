package org.example.eventmanager.events.api;

import org.example.eventmanager.events.domain.EventStatus;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public record EventSearchFilter (
        String name,
        Long placesMin,
        Long placesMax,
        ZonedDateTime dateStartAfter,
        ZonedDateTime dateStartBefore,
        BigDecimal costMin,
        BigDecimal costMax,
        Integer durationMin,
        Integer durationMax,
        Long locationId,
        EventStatus eventStatus
){
}
