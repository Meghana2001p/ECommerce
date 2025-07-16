package com.project.E_Commerce.Exception1;


import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1️⃣ Validation errors (e.g., from @Valid DTOs)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {
        ex.printStackTrace(); // Optional: log the full stack trace

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    // 2️⃣ Business logic errors (e.g., product already exists)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
        ex.printStackTrace(); // Optional: log the full stack trace

        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    // 3️⃣ Database access errors (e.g., constraint violations, JDBC failure)
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<String> handleDatabaseErrors(DataAccessException ex) {
        ex.printStackTrace(); // Optional: log the full stack trace

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Some problem occurred, please try again later");
    }

    // 4️⃣ Catch-all: Unexpected errors
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleUnexpected(Exception ex) {
        ex.printStackTrace(); // Optional: log the full stack trace
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Some problem occurred, please try again later.");
    }
}