package com.movie.reservation.movie_service.dto;
import com.movie.reservation.movie_service.model.Genre;
public record MovieRequest(
    String title,
    String description,
    String postedUrl,
    Genre genre
) {}
