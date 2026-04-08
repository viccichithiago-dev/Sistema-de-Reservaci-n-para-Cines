package com.movie.reservation.movie_service.exception;
import org.springframework.http.HttpStatus;
public class MovieNotFoundException extends BusinessException {
    public MovieNotFoundException(Long id) {
        super("Pelicula no encontrada con el ID: " + id, HttpStatus.NOT_FOUND.value());
    }
}
