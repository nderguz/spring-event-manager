package org.example.eventmanager.events.api;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

public record RequestEvent (

        @NotBlank
        ZonedDateTime date,

        @NotBlank
        @Min(value = 0, message = "Duration cannot be negative number")
        Integer duration,

        @Min(value = 1, message = "Minimum cost should be above 1")
        BigDecimal cost,

        @NotBlank(message = "maxPlaces must be above 5")
        Integer maxPlaces,

        @NotBlank(message = "Location id cannot be empty")
        Long locationId,

        @NotBlank(message = "Event name cannot be empty")
        String name
){
}
