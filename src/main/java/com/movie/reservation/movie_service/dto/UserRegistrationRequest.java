package com.movie.reservation.movie_service.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
public record UserRegistrationRequest(
    @Email
    @NotBlank
    String email,
    @NotBlank
    @Size(min = 6)
    String password,
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "El nombre de usuario solo puede contener letras, números y guiones bajos")
    String name
) {}
