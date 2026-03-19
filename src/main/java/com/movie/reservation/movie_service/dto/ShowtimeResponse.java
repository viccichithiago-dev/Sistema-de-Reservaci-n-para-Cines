package com.movie.reservation.movie_service.dto;

import java.time.LocalDateTime;

public record ShowtimeResponse(
    Long id,
    Long movieId,
    Long theaterId,
    LocalDateTime startTime,
    LocalDateTime endTime
) {}