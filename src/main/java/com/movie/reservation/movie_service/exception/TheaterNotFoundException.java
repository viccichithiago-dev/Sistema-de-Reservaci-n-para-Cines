package com.movie.reservation.movie_service.exception;
import org.springframework.http.HttpStatus;
public class TheaterNotFoundException extends BusinessException {
    public TheaterNotFoundException(Long id) {
        super("Teatro no encontrado con el ID: " + id, HttpStatus.NOT_FOUND.value());
    }
}
