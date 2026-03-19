package com.movie.reservation.movie_service.dto;

import java.util.List;

public record ReservationRequest(
    Long showtimeId,
    List<Long> seatIds
) {
    public ReservationRequest {
        if (showtimeId == null) {
            throw new IllegalArgumentException("showtimeId es requerido");
        }
        if (seatIds == null || seatIds.isEmpty()) {
            throw new IllegalArgumentException("Debe seleccionar al menos un asiento");
        }
        // Validar que no haya IDs de asiento duplicados
        if (seatIds.size() != seatIds.stream().distinct().count()) {
            throw new IllegalArgumentException("No se permiten IDs de asiento duplicados");
        }
    }
}