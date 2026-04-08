package com.movie.reservation.movie_service.exception;
import org.springframework.http.HttpStatus;
public class InvalidShowtimeTimeException extends BusinessException {
    public InvalidShowtimeTimeException(Long id) {
        super("Hora de función no válida para el ID: " + id, HttpStatus.BAD_REQUEST.value());
    }
}
