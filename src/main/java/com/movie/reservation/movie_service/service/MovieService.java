package com.movie.reservation.movie_service.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.movie.reservation.movie_service.dto.MovieRequest;
import com.movie.reservation.movie_service.dto.MovieResponse;
import com.movie.reservation.movie_service.model.Genre;

public interface MovieService {
    MovieResponse createMovie(MovieRequest request);
    MovieResponse getMovieById(Long id);
    Page<MovieResponse> getAllMovies(Pageable pageable, Optional<Genre> genre, Optional<String> searchTerm);
    MovieResponse updateMovie(Long id, MovieRequest request);
    void deleteMovie(Long id);
}
