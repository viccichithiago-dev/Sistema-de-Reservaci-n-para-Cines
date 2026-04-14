package com.movie.reservation.movie_service.controller;
import java.time.LocalDate;
import java.util.List;
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

import com.movie.reservation.movie_service.dto.AvailableSeatResponse;
import com.movie.reservation.movie_service.dto.ShowtimeRequest;
import com.movie.reservation.movie_service.dto.ShowtimeResponse;
import com.movie.reservation.movie_service.model.Genre;
import com.movie.reservation.movie_service.service.ShowtimeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
@RestController
@RequestMapping("/api/showtimes")
@RequiredArgsConstructor
@Tag(name = "Showtime Management", description = "Operaciones para la gestión de funciones de películas")
public class ShowtimeController {
    private final ShowtimeService showtimeService;
    @GetMapping
    @Operation(summary = "Obtener todos los showtimes", description = "Obtiene una lista paginada de todos los showtimes, con opciones de filtrado por fecha y género.")
    public ResponseEntity<Page<ShowtimeResponse>> getAllShowtimes(
        @RequestParam(required = false) LocalDate date,
        @RequestParam(required = false) Genre genre,
        @PageableDefault(size = 10) Pageable pageable
    ){
        Page<ShowtimeResponse> response = showtimeService.getAvailableShowtimes(Optional.ofNullable(date), Optional.ofNullable(genre), pageable);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Obtener un showtime por ID", description = "Obtiene los detalles de un showtime específico utilizando su ID.")
    public ResponseEntity<ShowtimeResponse> getShowtimeById(@PathVariable Long id){
        ShowtimeResponse response = showtimeService.getShowtimeById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crear un nuevo showtime", description = "Crea un nuevo showtime con la información")
    public ResponseEntity<ShowtimeResponse> createShowtime(@Valid @RequestBody ShowtimeRequest request){
        ShowtimeResponse response = showtimeService.createShowtime(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Actualizar un showtime existente", description = "Actualiza los detalles de un showtime existente utilizando su ID. Solo accesible para usuarios con rol ADMIN.")
    public ResponseEntity<ShowtimeResponse> updateShowtime(@PathVariable Long id, @Valid @RequestBody ShowtimeRequest request){
        ShowtimeResponse response = showtimeService.updateShowtime(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar un showtime", description = "Elimina un showtime específico utilizando su ID")
    public ResponseEntity<Void> deleteShowtime(@PathVariable Long id){
        showtimeService.deleteShowtime(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{showtimeId}/available-seats")
    @Operation(summary = "Obtener asientos disponibles para un showtime", description = "Obtiene una lista de asientos disponibles para un showtime específico utilizando su ID.")
    public ResponseEntity<List<AvailableSeatResponse>> getAvailableSeats(@PathVariable Long showtimeId){
        List<AvailableSeatResponse> response = showtimeService.getAvailableSeats(showtimeId);
        return ResponseEntity.ok(response);
    }
}
