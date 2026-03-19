package com.movie.reservation.movie_service.dto;
import com.movie.reservation.movie_service.model.Role;
public record UserDTO(
    Long id,
    String email,
    String password,
    Role role
) {}
