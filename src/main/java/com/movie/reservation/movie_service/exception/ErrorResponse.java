package com.movie.reservation.movie_service.exception;
import java.time.LocalDateTime;
// DTO para representar la respuesta de error en formato JSON
public record ErrorResponse(
    int status,
    String message,
    LocalDateTime timestamp
) {}
