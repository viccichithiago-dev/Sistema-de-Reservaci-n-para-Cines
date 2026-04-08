package com.movie.reservation.movie_service.exception;
import org.springframework.http.HttpStatus;
public class TheaterAlreadyExistsException extends BusinessException {
    public TheaterAlreadyExistsException(String name) {
        super("El teatro con el nombre '" + name + "' ya existe.", HttpStatus.CONFLICT.value());
    }
}
