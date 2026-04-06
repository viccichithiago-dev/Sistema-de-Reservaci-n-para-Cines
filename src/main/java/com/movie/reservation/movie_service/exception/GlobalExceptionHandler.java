package com.movie.reservation.movie_service.exception;
import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
@ControllerAdvice
public class GlobalExceptionHandler {
    // Excepcion personalizada para recursos no encontrados
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
            ex.getStatusCode(),
            ex.getMessage(),
            LocalDateTime.now()
        );
        return ResponseEntity.status(ex.getStatusCode()).body(error);
    }
    // Excepcion personalizada para recursos duplicados
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateResource(DuplicateResourceException ex) {
        ErrorResponse error = new ErrorResponse(
            ex.getStatusCode(),
            ex.getMessage(),
            LocalDateTime.now()
        );
        return ResponseEntity.status(ex.getStatusCode()).body(error);
    }
    // Excepcion personalizada para credenciales invalidas
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCredentials(InvalidCredentialsException ex) {
        ErrorResponse error = new ErrorResponse(
            ex.getStatusCode(),
            ex.getMessage(),
            LocalDateTime.now()
        );
        return ResponseEntity.status(ex.getStatusCode()).body(error);
    }
    // Manejo de excepcion de metodo/argumentos invalidos (validation errors)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .reduce((a,b) -> a + " | " + b)
            .orElse("Error de validacion");
        ErrorResponse error = new ErrorResponse(
            400,
            message,
            LocalDateTime.now()
        );
        return ResponseEntity.status(400).body(error);
    }
    // Manejo de excepcion generica para cualquier otro error no manejado
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        ErrorResponse error = new ErrorResponse(
            500,
            "Error interno del servidor: " + ex.getMessage(),
            LocalDateTime.now()
        );
        return ResponseEntity.status(500).body(error);
    }
}
