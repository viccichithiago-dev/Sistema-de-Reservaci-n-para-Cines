package com.movie.reservation.movie_service.exception;
import org.springframework.http.HttpStatus;
public class ShowtimeOverlapException extends BusinessException {
    public ShowtimeOverlapException(Long id) {
        super("Overlap en la hora de función para el ID: " + id, HttpStatus.CONFLICT.value());
    }

}
