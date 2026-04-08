package com.movie.reservation.movie_service.exception;
import org.springframework.http.HttpStatus;
public class SeatsNotBelongingToShowtimeException extends BusinessException {
    public SeatsNotBelongingToShowtimeException(Long id) {
        super("Los asientos no pertenecen a la función con el ID: " + id, HttpStatus.BAD_REQUEST.value());
    }
}
