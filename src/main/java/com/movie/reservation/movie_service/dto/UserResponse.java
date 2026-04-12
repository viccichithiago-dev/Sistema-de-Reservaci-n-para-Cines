package com.movie.reservation.movie_service.dto;
import com.movie.reservation.movie_service.model.Role;
public record UserResponse(
    Long id,
    String email,
    Role role
) {}
