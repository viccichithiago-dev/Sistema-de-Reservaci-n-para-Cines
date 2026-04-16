package com.movie.reservation.movie_service.exception;
import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex) {
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
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex) {
        ErrorResponse error = new ErrorResponse(
            403, // Código HTTP para "Prohibido"
            "No tienes permisos suficientes para realizar esta acción.",
            LocalDateTime.now()
        );
        return ResponseEntity.status(403).body(error);
    }
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
            404,
            ex.getMessage(),
            LocalDateTime.now()
        );
        return ResponseEntity.status(404).body(error);
    }
    @ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(org.springframework.http.converter.HttpMessageNotReadableException ex) {
        String message = "Error en el formato del JSON: El valor proporcionado para el género no es válido o está vacío.";
        
        // Opcional: puedes intentar extraer una causa más específica del error
        if (ex.getMessage().contains("Genre")) {
            message = "El género proporcionado no es válido. Los valores permitidos son: ACTION, COMEDY, DRAMA, HORROR, ROMANCE, SCIENTIFIC_FANTASY.";
        }

        ErrorResponse error = new ErrorResponse(
            400, // Bad Request es más apropiado que 500
            message,
            java.time.LocalDateTime.now()
        );
        return ResponseEntity.status(400).body(error);
    }
}
