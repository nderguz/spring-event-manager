package org.example.eventmanager.errorhandler;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.time.LocalDateTime;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<ServerMessageHelper> handleMethodArgumentNotValidException(final MethodArgumentNotValidException ex) {
        var message = new ServerMessageHelper(
                "Некорректный запрос",
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

    @ExceptionHandler(value = {EntityNotFoundException.class})
    public ResponseEntity<ServerMessageHelper> handleEntityNotFoundException(final EntityNotFoundException ex) {
        var message = new ServerMessageHelper(
                "Сущность не найдена",
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }
}
