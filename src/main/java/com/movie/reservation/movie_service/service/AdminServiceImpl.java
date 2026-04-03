package com.movie.reservation.movie_service.service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.movie.reservation.movie_service.dto.AdminStatisticsResponse;
import com.movie.reservation.movie_service.dto.MoviePopularityResponse;
import com.movie.reservation.movie_service.dto.ReservationResponse;
import com.movie.reservation.movie_service.model.Reservation;
import com.movie.reservation.movie_service.repository.MovieRepository;
import com.movie.reservation.movie_service.repository.ReservationRepository;
import com.movie.reservation.movie_service.repository.ShowtimeSeatRepository;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService{
    private final ReservationRepository reservationRepository;
    private final ShowtimeSeatRepository showtimeSeatRepository;
    private final MovieRepository movieRepository;
    private final ReservationMapper reservationMapper;

    // Metodo 1 obtener todas las reservaciones!
    @Override
    public Page<ReservationResponse> getAllReservations(Pageable pageable) {
        Page<Reservation> reservations = reservationRepository.findAll(pageable);
        return reservations.map(reservationMapper::toResponse);
    }
    // Metodo 2 Obtener estadisticas
    @Override
    public AdminStatisticsResponse getReservationStatistics(Optional<LocalDateTime> startDate, Optional<LocalDateTime> endDate){
        // Calculamos las estadisticas
        long totalReservations;
        BigDecimal totalRevenue;
        if(startDate.isPresent() && endDate.isPresent()){
            totalReservations = reservationRepository.countByReservationDateBetween(startDate.get(), endDate.get());
            totalRevenue = reservationRepository.sumRevenueByDateRange(startDate.get(), endDate.get());
        } else{
            totalReservations = reservationRepository.countConfirmedReservations();
            totalRevenue = reservationRepository.sumTotalRevenue();
        }
        // Calcular tasa de ocupacion
        long totalSeats = showtimeSeatRepository.countTotalSeats();
        long bookedSeats = showtimeSeatRepository.countBookedSeats();
        double occupancyRate = totalSeats > 0 ? (double) bookedSeats / totalSeats * 100 : 0.0;
        
        // Obtener peliculas populares
        List<MoviePopularityResponse> popularMovies = getMoviesByPopularity(startDate, endDate);
        return new AdminStatisticsResponse(
            totalReservations,
            totalRevenue,
            occupancyRate,
            popularMovies
        );
    }
    // Metodo para obtener peliculas por popularidad
    @Override
    public List<MoviePopularityResponse> getMoviesByPopularity(Optional<LocalDateTime> startDate, Optional<LocalDateTime> endDate){
        LocalDateTime start = startDate.orElse(LocalDateTime.MIN);
        LocalDateTime end = endDate.orElse(LocalDateTime.MAX);
        // Usamos el metodo del repo con constructor expression
        return movieRepository.findMoviesByPopularity(start,end);
    }
}
