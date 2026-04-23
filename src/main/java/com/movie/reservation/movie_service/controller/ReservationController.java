package com.movie.reservation.movie_service.controller;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.EntityModel;
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

import com.movie.reservation.movie_service.config.HateoasLinkBuilder;
import com.movie.reservation.movie_service.config.RoleHelper;
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
    private final RoleHelper roleHelper;
    private final HateoasLinkBuilder<ReservationResponse> hateoasLinkBuilder;

    @PostMapping
    @Operation(summary = "Crear una nueva reserva", description = "Crea una nueva reserva para un showtime específico. El usuario debe estar autenticado para realizar esta acción.")
    public ResponseEntity<EntityModel<ReservationResponse>> createReservation(
        @Valid @RequestBody ReservationRequest request,
        @AuthenticationPrincipal UserDetails userDetails
    ){
        UserResponse user = userService.getUserByEmail(userDetails.getUsername());
        ReservationResponse response = reservationService.createReservation(user.id(), request);
        
        boolean isAdmin = roleHelper.isAdmin(userDetails);
        EntityModel<ReservationResponse> model = hateoasLinkBuilder.buildLinks(
            response, "/api/reservations", response.id(), isAdmin);
            
        return ResponseEntity.status(HttpStatus.CREATED).body(model);
    }

    @GetMapping("/me")
    @Operation(summary = "Obtener mis reservas", description = "Obtiene una lista paginada de las reservas realizadas por el usuario autenticado.")
    public ResponseEntity<Page<EntityModel<ReservationResponse>>> getMyReservations(
        @AuthenticationPrincipal UserDetails userDetails,
        @PageableDefault(size = 10) Pageable pageable
    ){
        UserResponse user = userService.getUserByEmail(userDetails.getUsername());
        Page<ReservationResponse> reservations = reservationService.getUserReservations(user.id(), pageable);
        
        boolean isAdmin = roleHelper.isAdmin(userDetails);
        Page<EntityModel<ReservationResponse>> response = reservations.map(res -> 
            hateoasLinkBuilder.buildLinks(res, "/api/reservations", res.id(), isAdmin)
        );
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una reserva por ID", description = "Obtiene los detalles de una reserva específica utilizando su ID. El usuario debe ser el propietario de la reserva o tener rol ADMIN para acceder a esta información.")
    public ResponseEntity<EntityModel<ReservationResponse>> getReservationById(
        @PathVariable Long id,
        @AuthenticationPrincipal UserDetails userDetails
    ){
        ReservationResponse reservation = reservationService.getReservationById(id);
        UserResponse user = userService.getUserByEmail(userDetails.getUsername());
        boolean isAdmin = roleHelper.isAdmin(userDetails);
        boolean isOwner = reservation.userId().equals(user.id());
        if (!isAdmin && !isOwner) {
            throw new org.springframework.security.access.AccessDeniedException("No tienes permiso para acceder a esta reserva");
        }

        EntityModel<ReservationResponse> model = hateoasLinkBuilder.buildLinks(
            reservation, "/api/reservations", id, isAdmin);
            
        return ResponseEntity.ok(model);
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
