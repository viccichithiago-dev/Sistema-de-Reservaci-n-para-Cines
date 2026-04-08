package com.movie.reservation.movie_service.exception;
import org.springframework.http.HttpStatus;
public class SeatsAlreadyBookedException extends BusinessException {
    public SeatsAlreadyBookedException(Long id) {
        super("Los asientos ya están reservados para la función con el ID: " + id, HttpStatus.CONFLICT.value());
    }
}
