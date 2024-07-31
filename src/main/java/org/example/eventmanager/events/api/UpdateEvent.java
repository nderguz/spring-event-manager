package org.example.eventmanager.events.api;
import jakarta.annotation.Nullable;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public record UpdateEvent (
        @Nullable
        ZonedDateTime date,
        int duration,
        BigDecimal cost,
        Long maxPlaces,
        Long locationId,
        String name
){
}
