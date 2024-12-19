package org.example.eventmanager.errorhandler;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.text.ParseException;
import java.time.LocalDateTime;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<ServerMessageHelper> handleMethodArgumentNotValidException(final IllegalArgumentException ex) {
        log.error("Handle illegal argument exception", ex);
        var message = new ServerMessageHelper(
                "Выполнен запрос с невалидными данными",
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

    @ExceptionHandler(value = IllegalStateException.class)
    public ResponseEntity<ServerMessageHelper> handleIllegalStateException(final IllegalStateException ex) {
        log.error("Handle illegal state exception", ex);
        var message = new ServerMessageHelper(
                "Выполнен запрос с невалидными данными",
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

    @ExceptionHandler(value = EntityNotFoundException.class)
    public ResponseEntity<ServerMessageHelper> handleEntityNotFoundException(final EntityNotFoundException ex) {
        log.error("Handle not found exception", ex);
        var message = new ServerMessageHelper(
                "Сущность не найдена",
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ServerMessageHelper> handleException(final Exception ex) {
        log.error("Handle common exception", ex);
        var message = new ServerMessageHelper(
                "Внутренняя ошибка сервера",
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
    }

    @ExceptionHandler(value = BadCredentialsException.class)
    public ResponseEntity<ServerMessageHelper> handleBadCredentialsException(final BadCredentialsException ex) {
        log.error("Handle bad credentials exception", ex);
        var message = new ServerMessageHelper(
                "Необходима аутентификация",
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(401).body(message);
    }

    @ExceptionHandler(value = ParseException.class)
    public ResponseEntity<ServerMessageHelper> handleParseException(final ParseException ex) {
        log.error("Handle parse exception", ex);
        var message = new ServerMessageHelper(
                "Ошибка парсинга данных",
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }
}
