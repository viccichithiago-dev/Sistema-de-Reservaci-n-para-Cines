package com.movie.reservation.movie_service.dto;
import org.hibernate.validator.constraints.URL;

import com.movie.reservation.movie_service.model.Genre;
// Imports para validaciones
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;
public record MovieRequest(
    @NotBlank(message="El titulo es obligatorio")
    @Size(max=100, message="El titulo no puede tener mas de 100 caracteres")
    String title,
    @NotBlank(message="La descripcion es obligatoria")
    String description,
    @URL(message="La URL de la imagen no es valida")
    String postedUrl,
    @NotNull(message="El genero es obligatorio")
    Genre genre
) {}
