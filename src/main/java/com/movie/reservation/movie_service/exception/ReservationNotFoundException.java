package com.movie.reservation.movie_service.exception;
import org.springframework.http.HttpStatus;
public class ReservationNotFoundException extends BusinessException {
    public ReservationNotFoundException(Long id) {
        super("Reserva no encontrada con el ID: " + id, HttpStatus.NOT_FOUND.value());
    }
}
