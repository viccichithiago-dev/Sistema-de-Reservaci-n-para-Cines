package com.movie.reservation.movie_service.dto;

public record AvailableSeatResponse(
    Long showtimeSeatId,
    Long seatId,
    int seatNumber,
    int row,
    boolean booked,
    Double price
) {}
