package com.movie.reservation.movie_service.exception;
import org.springframework.http.HttpStatus;
public class InvalidCredentialsException extends BusinessException {
    public InvalidCredentialsException(String message) {
        super(message, HttpStatus.UNAUTHORIZED.value());
    }
}
