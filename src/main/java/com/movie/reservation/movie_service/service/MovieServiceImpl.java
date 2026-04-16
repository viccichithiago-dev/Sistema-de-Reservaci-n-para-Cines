package com.movie.reservation.movie_service.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.movie.reservation.movie_service.dto.MovieRequest;
import com.movie.reservation.movie_service.dto.MovieResponse;
import com.movie.reservation.movie_service.exception.DuplicateResourceException;
import com.movie.reservation.movie_service.exception.ResourceNotFoundException;
import com.movie.reservation.movie_service.model.Genre;
import com.movie.reservation.movie_service.model.Movie;
import com.movie.reservation.movie_service.model.spec.MovieSpecification;
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
                .orElseThrow(() -> new ResourceNotFoundException("Pelicula no encontrada con el ID: " + id));
        return mapToResponse(movie);            
    }
    
    // Metodo para obtener todas las peliculas con paginacion y filtros
    @Override
    public Page<MovieResponse> getAllMovies(Pageable pageable, Optional<Genre> genre, Optional<String> searchTerm) {
        Specification<Movie> spec = Specification.allOf();
        if (genre.isPresent()) {
            spec = spec.and(MovieSpecification.hasGenre(genre.get()));
        }
        if (searchTerm.isPresent()) {
            spec = spec.and(MovieSpecification.hasTitle(searchTerm.get()));
        }
        Page<Movie> moviesPage = movieRepository.findAll(spec, pageable);
        if (moviesPage.isEmpty()) {
            String message = "No se encontraron peliculas";
            if (genre.isPresent()) {
                message += " para el género: " + genre.get();
            }
            if (searchTerm.isPresent()) {
                message += " con el término de búsqueda (TITULO): '" + searchTerm.get() + "'";
            }
            throw new ResourceNotFoundException(message);
        }
        return moviesPage.map(this::mapToResponse);
    }

    // Metodo para Actualizar una Pelicula
    @Override
    @Transactional
    public MovieResponse updateMovie(Long id, MovieRequest request){
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pelicula no encontrada con el ID: " + id));
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
            throw new ResourceNotFoundException("La pelicula no se encontro, su ID es: "+ id);
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
        throw new DuplicateResourceException("Ya existe una pelicula con este genero y titulo");
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