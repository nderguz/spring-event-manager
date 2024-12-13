package org.example.eventmanager.events.api;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

public record RequestEvent (

        @NotNull
        @Future(message = "Event must be at future")
        LocalDateTime date,

        @NotNull
        @Positive(message = "Duration cannot be negative number")
        Integer duration,

        @Positive(message = "Minimum cost should be above 1")
        BigDecimal cost,

        @NotNull
        @Min(value = 5, message = "maxPlaces must be above 5")
        Long maxPlaces,

        @NotNull(message = "Location id cannot be empty")
        Long locationId,

        @NotNull(message = "Event name cannot be empty")
        String name
){
}
