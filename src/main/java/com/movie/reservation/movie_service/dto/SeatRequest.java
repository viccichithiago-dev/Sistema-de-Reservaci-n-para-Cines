package com.movie.reservation.movie_service.dto;

public record SeatRequest(
    int seatNumber,
    int row
) {}
