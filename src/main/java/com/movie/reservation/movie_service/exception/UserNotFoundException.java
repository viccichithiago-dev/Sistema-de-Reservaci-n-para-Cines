package com.movie.reservation.movie_service.exception;
import org.springframework.http.HttpStatus;
public class UserNotFoundException extends BusinessException {
    public UserNotFoundException(Long id) {
        super("Usuario no encontrado con el ID: " + id, HttpStatus.NOT_FOUND.value());
    }
}
