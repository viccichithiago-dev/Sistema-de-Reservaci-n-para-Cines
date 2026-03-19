package com.movie.reservation.movie_service.dto;

public record SeatResponse(
    Long id,
    int seatNumber,
    int row
) {}
