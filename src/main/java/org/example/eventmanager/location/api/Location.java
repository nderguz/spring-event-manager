package org.example.eventmanager.location.api;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;


@Builder
public record Location (
        Long id,

        @NotBlank(message = "Location name cannot be empty")
        String name,

        @NotBlank(message = "Location address cannot be empty")
        String address,

        @NotNull
        @Min(value = 5, message = "Capacity must be greater than 5")
        Long capacity,

        String description
){
}
