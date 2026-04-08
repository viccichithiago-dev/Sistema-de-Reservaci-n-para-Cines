package com.movie.reservation.movie_service.dto;
// Agrupamos resultados de las estadísticas de reservas en un solo DTO para facilitar su manejo en el controlador
import java.math.BigDecimal;
public record ReservationStats(
    long totalReservations,
    BigDecimal totalRevenue
) {}
