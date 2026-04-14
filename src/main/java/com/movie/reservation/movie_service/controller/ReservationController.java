package com.movie.reservation.movie_service.controller;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.movie.reservation.movie_service.dto.ReservationRequest;
import com.movie.reservation.movie_service.dto.ReservationResponse;
import com.movie.reservation.movie_service.dto.UserResponse;
import com.movie.reservation.movie_service.service.ReservationService;
import com.movie.reservation.movie_service.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
@Tag(name = "Reservation Management", description = "Operaciones para la gestión de reservas de asientos en funciones de películas")
public class ReservationController {
    private final ReservationService reservationService;
    private final UserService userService;

    @PostMapping
    @Operation(summary = "Crear una nueva reserva", description = "Crea una nueva reserva para un showtime específico. El usuario debe estar autenticado para realizar esta acción.")
    public ResponseEntity<ReservationResponse> createReservation(
        @Valid @RequestBody ReservationRequest request,
        @AuthenticationPrincipal UserDetails userDetails
    ){
        UserResponse user = userService.getUserByEmail(userDetails.getUsername());
        ReservationResponse response = reservationService.createReservation(user.id(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/me")
    @Operation(summary = "Obtener mis reservas", description = "Obtiene una lista paginada de las reservas realizadas por el usuario autenticado.")
    public ResponseEntity<Page<ReservationResponse>> getMyReservations(
        @AuthenticationPrincipal UserDetails userDetails,
        @PageableDefault(size = 10) Pageable pageable
    ){
        UserResponse user = userService.getUserByEmail(userDetails.getUsername());
        Page<ReservationResponse> response = reservationService.getUserReservations(user.id(), pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una reserva por ID", description = "Obtiene los detalles de una reserva específica utilizando su ID. El usuario debe ser el propietario de la reserva o tener rol ADMIN para acceder a esta información.")
    public ResponseEntity<ReservationResponse> getReservationById(
        @PathVariable Long id,
        @AuthenticationPrincipal UserDetails userDetails
    ){
        ReservationResponse reservation = reservationService.getReservationById(id);
        // Verificar que el usuario autenticado sea el propietario de la reserva o tenga rol ADMIN
        UserResponse user = userService.getUserByEmail(userDetails.getUsername());
        boolean isAdmin = user.role().name().equals("ROLE_ADMIN");
        boolean isOwner = reservation.userId().equals(user.id());
        if (!isAdmin && !isOwner) {
            throw new org.springframework.security.access.AccessDeniedException("No tienes permiso para acceder a esta reserva");
        }
        return ResponseEntity.ok(reservation);
    }
    @DeleteMapping("/{id}")
    @Operation(summary = "Cancelar una reserva", description = "Cancela una reserva específica utilizando su ID. El usuario debe ser el propietario de la reserva o tener rol ADMIN para realizar esta acción.")
    public ResponseEntity<Void> cancelReservation(
        @AuthenticationPrincipal UserDetails userDetails,
        @PathVariable Long id
    ){
        ReservationResponse reservation = reservationService.getReservationById(id);
        // Verificar ownership: usuario es propietario o Admin
        UserResponse user = userService.getUserByEmail(userDetails.getUsername());
        boolean isAdmin = user.role().name().equals("ROLE_ADMIN");
        boolean isOwner = reservation.userId().equals(user.id());
        if (!isAdmin && !isOwner) {
            throw new org.springframework.security.access.AccessDeniedException("No tienes permiso para cancelar esta reserva");
        }
        reservationService.cancelReservation(id);
        return ResponseEntity.noContent().build();
    }
}
