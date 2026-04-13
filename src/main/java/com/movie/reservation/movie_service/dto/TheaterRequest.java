package com.movie.reservation.movie_service.dto;

import jakarta.validation.constraints.NotBlank;

public record TheaterRequest(
    @NotBlank String name,
    @NotBlank String location
) {}