package com.movie.reservation.movie_service.exception;
import org.springframework.http.HttpStatus;
public class DuplicateResourceException extends BusinessException {
    public DuplicateResourceException(String message) {
        super(message, HttpStatus.CONFLICT.value());
    }
}
