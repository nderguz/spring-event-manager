package org.example.eventmanager.events.api.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record EventUpdateRequest(

        @JsonFormat(shape = JsonFormat.Shape.STRING)
        @Future(message = "Date must be in future")
        @NotNull
        LocalDateTime date,

        @Min(value = 30, message = "Duration must be greater than 30")
        @NotNull
        Integer duration,
        @PositiveOrZero(message = "Cost must be non-negative")
        @NotNull
        BigDecimal cost,
        @NotNull
        @Positive(message = "Maximum places must be greater than zero")
        Long maxPlaces,
        @NotNull
        Long locationId,
        @NotNull
        String name
) {
}
