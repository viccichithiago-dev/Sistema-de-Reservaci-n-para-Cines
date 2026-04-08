package com.movie.reservation.movie_service.service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.movie.reservation.movie_service.dto.AdminStatisticsResponse;
import com.movie.reservation.movie_service.dto.MoviePopularityResponse;
import com.movie.reservation.movie_service.dto.ReservationResponse;
import com.movie.reservation.movie_service.dto.ReservationStats;
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
    @Transactional(readOnly = true)
    public Page<ReservationResponse> getAllReservations(Pageable pageable) {
        Page<Reservation> reservations = reservationRepository.findAll(pageable);
        return reservations.map(reservationMapper::toResponse);
    }
    // Metodo 2 Obtener estadisticas
    @Override
    @Transactional(readOnly = true)
    public AdminStatisticsResponse getReservationStatistics(Optional<LocalDateTime> startDate, Optional<LocalDateTime> endDate){
        ReservationStats stats = calculateReservationStats(startDate, endDate);
        double occupancyRate = calculateOccupancyRate();
        // Obtener peliculas populares
        List<MoviePopularityResponse> popularMovies = getMoviesByPopularity(startDate, endDate);
        return new AdminStatisticsResponse(
            stats.totalReservations(),
            stats.totalRevenue(),
            occupancyRate,
            popularMovies
        );
    }
     // Metodo privado para calcular las estadisticas de las reservas
    private ReservationStats calculateReservationStats(Optional<LocalDateTime> startDate, Optional<LocalDateTime> endDate){
        if(startDate.isPresent() && endDate.isPresent()){
            long total = reservationRepository.countByReservationDateBetween(startDate.get(), endDate.get());
            BigDecimal revenue = reservationRepository.sumRevenueByDateRange(startDate.get(), endDate.get());
            return new ReservationStats(total, revenue);
        
        } else {
            long total = reservationRepository.countConfirmedReservations();
            BigDecimal revenue = reservationRepository.sumTotalRevenue();
            return new ReservationStats(total, revenue);
        }
    }
    // Metodo privado para calcular la ocupacion
    private double calculateOccupancyRate(){
        long totalSeats = showtimeSeatRepository.count();
        long bookedSeats = showtimeSeatRepository.countBookedSeats();
        return totalSeats == 0 ? 0 : (double) bookedSeats / totalSeats * 100;
    }
    // Metodo para obtener peliculas por popularidad
    @Override
    @Transactional(readOnly = true)
    public List<MoviePopularityResponse> getMoviesByPopularity(Optional<LocalDateTime> startDate, Optional<LocalDateTime> endDate){
        LocalDateTime start = startDate.orElse(LocalDateTime.MIN);
        LocalDateTime end = endDate.orElse(LocalDateTime.MAX);
        // Usamos el metodo del repo con constructor expression
        return movieRepository.findMoviesByPopularity(start,end);
    }
}
