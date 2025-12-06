package com.trillo.innovesync.exception;

import java.time.Instant;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.util.pattern.PatternParseException;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidCredentials(InvalidCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of(
                        "timestamp", Instant.now().toString(),
                        "status", HttpStatus.UNAUTHORIZED.value(),
                        "error", "Invalid credentials",
                        "message", ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .orElse("Validation failed");
        return ResponseEntity.badRequest()
                .body(Map.of(
                        "timestamp", Instant.now().toString(),
                        "status", HttpStatus.BAD_REQUEST.value(),
                        "error", "Validation error",
                        "message", message));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.badRequest()
                .body(Map.of(
                        "timestamp", Instant.now().toString(),
                        "status", HttpStatus.BAD_REQUEST.value(),
                        "error", "Bad request",
                        "message", ex.getMessage()));
    }

    @ExceptionHandler(PatternParseException.class)
    public ResponseEntity<Map<String, Object>> handlePatternParse(PatternParseException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                        "timestamp", Instant.now().toString(),
                        "status", HttpStatus.BAD_REQUEST.value(),
                        "error", "Invalid request path",
                        "message", ex.getMessage()));
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<Map<String, Object>> handleDataAccess(DataAccessException ex) {
        String message = "Database connection error. Please ensure MongoDB is running on localhost:27017";
        if (ex.getMessage() != null && ex.getMessage().contains("Connection refused")) {
            message = "Cannot connect to MongoDB. Please ensure MongoDB is running on localhost:27017";
        }
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Map.of(
                        "timestamp", Instant.now().toString(),
                        "status", HttpStatus.SERVICE_UNAVAILABLE.value(),
                        "error", "Database error",
                        "message", message));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalState(IllegalStateException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Map.of(
                        "timestamp", Instant.now().toString(),
                        "status", HttpStatus.FORBIDDEN.value(),
                        "error", "Forbidden",
                        "message", ex.getMessage()));
    }
}
