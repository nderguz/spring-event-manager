package org.example.eventmanager.events.api;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public record UpdateEvent (

        @JsonFormat(shape = JsonFormat.Shape.STRING)
        @Future(message = "Date must be in future")
        ZonedDateTime date,

        @Min(value = 30, message = "Duration must be greater than 30")
        Integer duration,
        @PositiveOrZero(message = "Cost must be non-negative")
        BigDecimal cost,
        @Positive(message = "Maximum places must be greater than zero")
        Long maxPlaces,
        Long locationId,
        String name
){
}
