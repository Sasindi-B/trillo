package com.trillo.innovesync.exception;

import java.time.Instant;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

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
}
