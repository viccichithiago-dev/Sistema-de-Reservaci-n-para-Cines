package com.movie.reservation.movie_service.dto;

import java.time.LocalDateTime;

public record ShowtimeRequest(
    Long movieId,
    Long theaterId,
    LocalDateTime startTime,
    LocalDateTime endTime
) {
    public ShowtimeRequest {
        if (movieId == null) {
            throw new IllegalArgumentException("movieId is required");
        }
        if (theaterId == null) {
            throw new IllegalArgumentException("theaterId is required");
        }
        if (startTime == null) {
            throw new IllegalArgumentException("startTime is required");
        }
        if (endTime == null) {
            throw new IllegalArgumentException("endTime is required");
        }
        if (!startTime.isBefore(endTime)) {
            throw new IllegalArgumentException("startTime must be before endTime");
        }
    }
}