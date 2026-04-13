package com.movie.reservation.movie_service.dto;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record ReservationRequest(
    @NotNull(message = "El showtimeId es requerido")
    Long showtimeId,
    @NotEmpty(message = "Debe seleccionar al menos un asiento")
    List<Long> seatIds
) {}