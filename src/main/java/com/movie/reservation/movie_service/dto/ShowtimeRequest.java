package com.movie.reservation.movie_service.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;

public record ShowtimeRequest(
    @NotNull(message = "El movieId es requerido")
    Long movieId,
    @NotNull(message = "El theaterId es requerido")
    Long theaterId,
    @NotNull(message = "El startTime es requerido")
    LocalDateTime startTime,
    @NotNull(message = "El endTime es requerido")
    LocalDateTime endTime
) {}