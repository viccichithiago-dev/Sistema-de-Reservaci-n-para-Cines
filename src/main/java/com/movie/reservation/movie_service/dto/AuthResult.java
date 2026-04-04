package com.movie.reservation.movie_service.dto;
import com.movie.reservation.movie_service.model.Role;
public record AuthResult(
    String token,
    String email,
    Role role
) {
}
