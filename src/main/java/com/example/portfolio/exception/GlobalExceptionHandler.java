package com.example.portfolio.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
        AlertNotFoundException.class,
        PortfolioNotFoundException.class,
        HoldingNotFoundException.class,
        FileGenerationException.class
    })
    public ResponseEntity<?> handleCustomExceptions(RuntimeException ex) {
        return ResponseEntity.badRequest().body(Map.of(
            "error", ex.getMessage(),
            "timestamp", LocalDateTime.now()
        ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleAllOtherExceptions(Exception ex) {
        return ResponseEntity.internalServerError().body(Map.of(
            "error", "Internal Server Error",
            "message", ex.getMessage(),
            "timestamp", LocalDateTime.now()
        ));
    }
}
