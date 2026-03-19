package com.movie.reservation.movie_service.dto;

import java.time.LocalDateTime;

public record ShowtimeDetailResponse(
    Long id,
    Long movieId,
    Long theaterId,
    LocalDateTime startTime,
    LocalDateTime endTime,
    MovieResponse movie,
    TheaterResponse theater,
    long availableSeatsCount
) {}