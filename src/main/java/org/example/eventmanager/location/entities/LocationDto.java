package org.example.eventmanager.location.entities;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LocationDto(
        Long Id,

        @NotBlank(message = "Location name cannot be empty")
        String name,

        @NotBlank(message = "Location address cannot be empty")
        String address,

        @NotNull
        @Min(value = 5, message = "Capacity must be greater than 5")
        Long capacity,

        String description
) {
}
