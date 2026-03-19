package com.movie.reservation.movie_service.dto;

public record UserRegistrationRequest(
    String email,
    String password,
    String name
) {}
