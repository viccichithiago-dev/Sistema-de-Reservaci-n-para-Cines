package com.movie.reservation.movie_service.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.movie.reservation.movie_service.dto.MovieRequest;
import com.movie.reservation.movie_service.dto.MovieResponse;
import com.movie.reservation.movie_service.model.Genre;
import com.movie.reservation.movie_service.model.Movie;
import com.movie.reservation.movie_service.repository.MovieRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {
    private final MovieRepository movieRepository;

    // Metodo para crear una pelicula
    @Override
    @Transactional
    public MovieResponse createMovie(MovieRequest request){
        validateDuplicateMovie(request.title(), request.genre());
        // Pasamos los datos del DTO a la Entity
        Movie movie = new Movie(
                request.title(),
                request.description(),
                request.postedUrl(),
                request.genre()
        );

        // Lo guardamos en la base de datos!
        Movie savedMovie = movieRepository.save(movie);
        return mapToResponse(savedMovie);
    }

    // Metodo para obtener la pelicula por el ID
    @Override
    public MovieResponse getMovieById(Long id){
        // Validacion
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pelicula no encontrada con el ID: " + id));
        return mapToResponse(movie);            
    }
    
    // Metodo para obtener todas las peliculas con paginacion y filtros
    @Override
    public Page<MovieResponse> getAllMovies(Pageable pageable, Optional<Genre> genre, Optional<String> searchTerm) {
        if (genre.isPresent() && searchTerm.isPresent()) {
           
            return movieRepository.findAllByTitleContainingIgnoreCaseAndGenre(searchTerm.get(), genre.get(), pageable)
                    .map(this::mapToResponse);
        } else if (genre.isPresent()) {
            
            return movieRepository.findAllByGenre(genre.get(), pageable)
                    .map(this::mapToResponse);
        } else if (searchTerm.isPresent()) {
            
            return movieRepository.findAllByTitleContainingIgnoreCase(searchTerm.get(), pageable)
                    .map(this::mapToResponse);
        } else {
            
            return movieRepository.findAll(pageable)
                    .map(this::mapToResponse);
        }
    }

    // Metodo para Actualizar una Pelicula
    @Override
    @Transactional
    public MovieResponse updateMovie(Long id, MovieRequest request){
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found with ID: " + id));
        // Actualizar
        movie.setTitle(request.title());
        movie.setDescription(request.description());
        movie.setPosterUrl(request.postedUrl());
        movie.setGenre(request.genre());
        Movie updatedMovie = movieRepository.save(movie);
        return mapToResponse(updatedMovie);
    }
    // Metodo para eliminar una pelicula
    @Override
    @Transactional
    public void deleteMovie(Long id){
        if(!movieRepository.existsById(id)){
            throw new RuntimeException("La pelicula no se encontro, su ID es: "+ id);
        }
        movieRepository.deleteById(id);
    }   
    // Metodos privados para la ayuda
   private void validateDuplicateMovie(String title, Genre genre, Long excludeId) {
    boolean exists = movieRepository.existsByTitleAndGenre(title, genre);

    if (exists && excludeId != null) {
        // Buscamos de forma tradicional para poder reasignar 'exists'
        var existingMovie = movieRepository.findById(excludeId);
        if (existingMovie.isPresent() && existingMovie.get().getId().equals(excludeId)) {
            exists = false;
        }
    }

    if (exists) {
        throw new RuntimeException("Ya existe una pelicula con este genero y titulo");
    }
}
    
    private void validateDuplicateMovie(String title, Genre genre) {
        validateDuplicateMovie(title, genre, null);
    }
     
    private MovieResponse mapToResponse(Movie movie) {
        return new MovieResponse(
                movie.getId(),
                movie.getTitle(),
                movie.getDescription(),
                movie.getPosterUrl(),
                movie.getGenre()
            );
    }
}