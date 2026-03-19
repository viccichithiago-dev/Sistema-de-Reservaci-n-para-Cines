package com.movie.reservation.movie_service.dto;

public record LoginRequest(
    String email,
    String password
) {}
