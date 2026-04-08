package com.movie.reservation.movie_service.exception;
import org.springframework.http.HttpStatus;
public class SeatNotFoundException extends BusinessException {
    public SeatNotFoundException(Long id) {
        super("Asiento no encontrado con el ID: " + id, HttpStatus.NOT_FOUND.value());
    }
}
