package com.movie.reservation.movie_service.service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.movie.reservation.movie_service.dto.AdminStatisticsResponse;
import com.movie.reservation.movie_service.dto.MoviePopularityResponse;
import com.movie.reservation.movie_service.dto.ReservationResponse;
public interface AdminService {
    // Obtener todas las reservaciones
    Page<ReservationResponse> getAllReservations(Pageable pageable);

    // Metodo 2 Obtener estadisticas de reservaciones
    AdminStatisticsResponse getReservationStatistics(
        Optional<LocalDateTime> startDate,
        Optional<LocalDateTime> endDate
    );
    // Metodo para obtener las peliculas por su popularidad
    List<MoviePopularityResponse> getMoviesByPopularity(
        Optional<LocalDateTime> startDate,
        Optional<LocalDateTime> endDate
    );
}
