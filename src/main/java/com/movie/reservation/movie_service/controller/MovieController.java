package com.movie.reservation.movie_service.controller;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.movie.reservation.movie_service.dto.MovieRequest;
import com.movie.reservation.movie_service.dto.MovieResponse;
import com.movie.reservation.movie_service.model.Genre;
import com.movie.reservation.movie_service.service.MovieService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
@Tag(name = "Movie Management", description = "Operaciones para la gestión de películas, incluyendo creación, actualización, eliminación y consulta de películas.")
public class MovieController {
    private final MovieService movieService;
    // Endpoint para crear una pelicula
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crear una nueva película", description = "Crea una nueva película con la información proporcionada")
    public ResponseEntity<MovieResponse> createMovie(@Valid @RequestBody MovieRequest request){
        MovieResponse response = movieService.createMovie(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    // Endpoint para obtener una pelicula por ID
    @GetMapping("/{id}")
    @Operation(summary = "Obtener una película por ID", description = "Obtiene los detalles de una película específica por su ID")
    public ResponseEntity<MovieResponse> getMovieById(@PathVariable Long id){
        MovieResponse response = movieService.getMovieById(id);
        return ResponseEntity.ok(response);
    }
    // Endpoint para obtener todas las peliculas con paginacion y filtros
    @GetMapping
    @Operation(summary = "Obtener todas las películas", description = "Obtiene una lista paginada de todas las películas, con opciones de filtrado por género y término de búsqueda en el título.")
    public ResponseEntity<Page<MovieResponse>> getAllMovies(
        @PageableDefault(size = 10) Pageable pageable,
        @RequestParam(required = false) Genre genre,
        @RequestParam(required = false) String searchTerm
    ){
        Page<MovieResponse> response = movieService.getAllMovies(
            pageable,
            Optional.ofNullable(genre),
            Optional.ofNullable(searchTerm)
        );
        return ResponseEntity.ok(response);

    }
    // Endpoint para actualizar una pelicula
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Actualizar una película existente", description = "Actualiza la información de una película existente utilizando su ID")
    public ResponseEntity<MovieResponse> updateMovie(@PathVariable Long id, @Valid @RequestBody MovieRequest request){
        MovieResponse response = movieService.updateMovie(id, request);
        return ResponseEntity.ok(response);
    }
    // Endpoint para eliminar una pelicula
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar una película", description = "Elimina una película existente utilizando su ID")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id){
        movieService.deleteMovie(id);
        return ResponseEntity.noContent().build();
    }
}
