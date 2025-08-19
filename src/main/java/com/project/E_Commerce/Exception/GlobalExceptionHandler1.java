package com.project.E_Commerce.Exception;
import com.project.E_Commerce.ServiceImplementation.Cart.CartServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler1 {


    private static final Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);


    //Validation errors (e.g., from @Valid DTOs)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {
        ex.printStackTrace();
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

        logger.error("Invalid input");
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    // Business logic errors (e.g., product already exists)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
        logger.error(String.valueOf(ex));
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    //Database access errors (e.g., constraint violations, JDBC failure)
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<String> handleDatabaseErrors(DataAccessException ex) {
        logger.error(String.valueOf(ex));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Some problem occurred, please try again later");
    }

    //Catch-all: Unexpected errors
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleUnexpected(Exception ex) {
        logger.error(String.valueOf(ex));

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Some problem occurred, please try again later.");
    }


    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> handleAuthenticationException(AuthenticationException ex) {
        logger.error("Authentication failed: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Unauthorized: Invalid credentials or authentication required");
    }



    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<?> handleResourceAlreadyExists(ResourceAlreadyExistsException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT) // 409
                .body(Collections.singletonMap("error", ex.getMessage()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDenied(AccessDeniedException ex) {
        logger.error("Access denied: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Collections.singletonMap("error", "Forbidden: " + ex.getMessage()));
    }

}