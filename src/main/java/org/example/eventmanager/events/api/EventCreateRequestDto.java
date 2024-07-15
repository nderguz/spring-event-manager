package org.example.eventmanager.events.api;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public record EventCreateRequestDto (
        @NotNull(message = "Date is mandatory")
        LocalDateTime date,

        @Min(value = 30, message = "Duration must be at least 30 minutes")
        Long duration,

        @PositiveOrZero(message = "Cost must be non-negative")
        Long cost,

        @Positive(message = "Maximum places must be greater than zero")
        Long maxPlaces,

        @NotNull(message = "Location ID is mandatory")
        Long locationId,

        @NotBlank(message = "Name is mandatory")
        String name
){
}
