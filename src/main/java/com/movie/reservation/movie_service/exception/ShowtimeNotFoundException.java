package com.movie.reservation.movie_service.exception;
import org.springframework.http.HttpStatus;
public class ShowtimeNotFoundException extends BusinessException {
    public ShowtimeNotFoundException(Long id) {
        super("Funcion no encontrada con el ID: " + id, HttpStatus.NOT_FOUND.value());
    }
}
