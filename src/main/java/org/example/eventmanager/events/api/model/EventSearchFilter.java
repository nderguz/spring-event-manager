package org.example.eventmanager.events.api.model;

import org.example.eventmanager.events.domain.model.EventStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record EventSearchFilter (
        String name,
        Long placesMin,
        Long placesMax,
        LocalDateTime dateStartAfter,
        LocalDateTime dateStartBefore,
        BigDecimal costMin,
        BigDecimal costMax,
        Integer durationMin,
        Integer durationMax,
        Long locationId,
        EventStatus eventStatus
){
}
