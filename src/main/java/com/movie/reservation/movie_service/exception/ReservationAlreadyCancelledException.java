package com.movie.reservation.movie_service.exception;
import org.springframework.http.HttpStatus;
public class ReservationAlreadyCancelledException extends BusinessException {
    public ReservationAlreadyCancelledException(Long id) {
        super("La reserva con el ID: " + id + " ya está cancelada.", HttpStatus.BAD_REQUEST.value());
    }
}
