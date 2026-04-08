package com.movie.reservation.movie_service.exception;
import org.springframework.http.HttpStatus;
public class InvalidCancellationException extends BusinessException {
    public InvalidCancellationException(Long id) {
        super("No se puede cancelar la reserva con el ID: " + id + " porque ya ha pasado la hora de la función.", HttpStatus.BAD_REQUEST.value());
    }

}
