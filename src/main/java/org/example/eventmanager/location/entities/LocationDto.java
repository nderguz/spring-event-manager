package org.example.eventmanager.location.entities;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record LocationDto(

        Long Id,

        @NotBlank(message = "Location name cannot be empty")
        String name,

        @NotBlank(message = "Location address cannot be empty")
        String address,

        @Min(value = 1, message = "Capacity must be greater than 0")
        Long capacity,

        String description
) {
}
