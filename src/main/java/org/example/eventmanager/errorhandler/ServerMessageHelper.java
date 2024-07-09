package org.example.eventmanager.errorhandler;

import java.time.LocalDateTime;

public record ServerMessageHelper (
        String message,
        String detailedMessage,
        LocalDateTime timestamp
){
}
