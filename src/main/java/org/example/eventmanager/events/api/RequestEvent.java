package org.example.eventmanager.events.api;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public record RequestEvent (

        @NotBlank
        LocalDateTime date,

        @NotBlank
        @Min(value = 0, message = "Duration cannot be negative number")
        Integer duration,

        @Min(value = 1, message = "Minimum cost should be above 1")
        Integer cost,

        @NotBlank(message = "maxPlaces must be above 5")
        Integer maxPlaces,

        Long locationId,

        @NotBlank(message = "Event name cannot be empty")
        String name
){
}
