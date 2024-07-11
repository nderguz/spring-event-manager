package org.example.eventmanager.errorhandler;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record ServerMessageHelper (
        String message,
        String detailedMessage,
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        LocalDateTime timestamp
){
}
