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

import com.movie.reservation.movie_service.dto.SeatResponse;
import com.movie.reservation.movie_service.dto.TheaterRequest;
import com.movie.reservation.movie_service.dto.TheaterResponse;
import com.movie.reservation.movie_service.service.TheaterService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
@RestController
@RequestMapping("/api/theaters")
@RequiredArgsConstructor
@Tag(name = "Theater Management", description = "Operaciones para la gestión de cines/teatros")
public class TheaterController {
    private final TheaterService theaterService;

    // Endpoint para obtener todos los teatros con paginacion
    @GetMapping
    @Operation(summary = "Obtener todos los teatros", description = "Obtiene una lista paginada de todos los teatros, con opciones de filtrado por nombre y ubicación.")
    public ResponseEntity<Page<TheaterResponse>> getAllTheaters(
        @PageableDefault(size = 10) Pageable pageable,
        @RequestParam(required = false) String name,
        @RequestParam(required = false) String location
    ){
        Page<TheaterResponse> response = theaterService.getAllTheaters(pageable, Optional.ofNullable(name), Optional.ofNullable(location));
        return ResponseEntity.ok(response);
    }
    // EndPoint para obtener un teatro por su id
    @GetMapping("/{id}")
    @Operation(summary = "Obtener un teatro por ID", description = "Obtiene los detalles de un teatro específico utilizando su ID.")
    public ResponseEntity<TheaterResponse> getTheaterById(@PathVariable Long id){
        TheaterResponse response = theaterService.getTheaterById(id);
        return ResponseEntity.ok(response);
    }
    // Endpoint para crear un nuevo teatro
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crear un nuevo teatro", description = "Crea un nuevo teatro con la información proporcionada. Solo accesible para usuarios con rol ADMIN.")
    public ResponseEntity<TheaterResponse> createTheater(@Valid @RequestBody TheaterRequest request){
        TheaterResponse response = theaterService.createTheater(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    // Endpoint para actualizar un teatro existente
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Actualizar un teatro existente", description = "Actualiza la información de un teatro existente utilizando su ID. Solo accesible para usuarios con rol ADMIN.")
    public ResponseEntity<TheaterResponse> updateTheater(@PathVariable Long id, 
        @Valid @RequestBody TheaterRequest request){
        TheaterResponse response = theaterService.updateTheater(id, request);
        return ResponseEntity.ok(response);
    }
    // Endpoint para eliminar un teatro
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar un teatro", description = "Elimina un teatro específico utilizando su ID. Solo accesible para usuarios con rol ADMIN.")
    public ResponseEntity<Void> deleteTheater(@PathVariable Long id){
        theaterService.deleteTheater(id);
        return ResponseEntity.noContent().build();
    }
    // Endpoint para obtener los asientos de un teatro
    @GetMapping("/{id}/seats")
    @Operation(summary = "Obtener los asientos de un teatro", description = "Obtiene una lista de los asientos disponibles en un teatro específico utilizando su ID.")
    public ResponseEntity<java.util.List<SeatResponse>> getSeatsByTheaterId(@PathVariable Long id){
        java.util.List<SeatResponse> response = theaterService.getTheaterSeats(id);
        return ResponseEntity.ok(response);
    }
}
