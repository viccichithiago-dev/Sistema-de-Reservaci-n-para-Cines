package com.movie.reservation.movie_service.dto;

public record AuthResponse(
    String token,
    String email,
    String role,
    long expiresIn
) {}
