package com.movie.reservation.movie_service.controller;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.movie.reservation.movie_service.dto.AdminStatisticsResponse;
import com.movie.reservation.movie_service.dto.ReservationResponse;
import com.movie.reservation.movie_service.service.AdminService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Tag(name = "Admin Management", description = "Operaciones para la gestión administrativa del sistema, incluyendo estadísticas y gestión de reservas.")
public class AdminController {
    private final AdminService adminService;

    @GetMapping("/reservations")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Obtener todas las reservas", description = "Obtiene una lista paginada de todas las reservas realizadas en el sistema. Solo accesible para usuarios con rol ADMIN.")
    public ResponseEntity<Page<ReservationResponse>> getAllReservations(
        @PageableDefault(size = 10) Pageable pageable
    ){
        Page<ReservationResponse> reservations = adminService.getAllReservations(pageable);
        return ResponseEntity.ok(reservations);
    }
    @GetMapping("/statistics")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminStatisticsResponse> getStatistics(
        @RequestParam(required = false) LocalDateTime start,
        @RequestParam(required = false) LocalDateTime end
    ){
        AdminStatisticsResponse statistics = adminService.getReservationStatistics(
            Optional.ofNullable(start), Optional.ofNullable(end));
        return ResponseEntity.ok(statistics);
    }
}
