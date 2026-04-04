package com.movie.reservation.movie_service.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.movie.reservation.movie_service.model.Reservation;
import com.movie.reservation.movie_service.model.ReservationStatus;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    
    // Find by user
    List<Reservation> findByUserId(Long userId);
    Page<Reservation> findByUserId(Long userId, Pageable pageable);
    List<Reservation> findByUserIdOrderByReservationDateDesc(Long userId);
    
    // Find by date range (REQUESTED)
    List<Reservation> findByReservationDateBetween(LocalDateTime from, LocalDateTime to);
    Page<Reservation> findByReservationDateBetween(LocalDateTime from, LocalDateTime to, Pageable pageable);
    
    // Combined user and date range
    List<Reservation> findByUserIdAndReservationDateBetween(Long userId, LocalDateTime from, LocalDateTime to);
    Page<Reservation> findByUserIdAndReservationDateBetween(Long userId, LocalDateTime from, LocalDateTime to, Pageable pageable);
    
    // Find by status
    List<Reservation> findByStatus(ReservationStatus status);
    Page<Reservation> findByStatus(ReservationStatus status, Pageable pageable);
    List<Reservation> findByUserIdAndStatus(Long userId, ReservationStatus status);
    Page<Reservation> findByUserIdAndStatus(Long userId, ReservationStatus status, Pageable pageable);
    
    // Find by user and status with date range (common admin query)
    List<Reservation> findByUserIdAndStatusAndReservationDateBetween(
            Long userId, ReservationStatus status, LocalDateTime from, LocalDateTime to);
    Page<Reservation> findByUserIdAndStatusAndReservationDateBetween(
            Long userId, ReservationStatus status, LocalDateTime from, LocalDateTime to, Pageable pageable);
    
    // Standard pagination and sorting
    Page<Reservation> findAll(Pageable pageable);
    List<Reservation> findAllByOrderByReservationDateDesc();
    
    // Existence checks
    boolean existsByUserId(Long userId);
    boolean existsById(Long id);
    // Metodos de consultas JQPL
    // Conteo total de reservaciones confirmadas
    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.status = 'CONFIRMED'")
    long countConfirmedReservations();
    
    // Ingresos totales
    @Query("SELECT COALESCE(SUM(r.totalAmount), 0) FROM Reservation r WHERE r.status = 'CONFIRMED'")
    BigDecimal sumTotalRevenue();

    //Conteo por rango de fechas
    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.reservationDate BETWEEN :startDate AND :endDate")
    long countByReservationDateBetween(
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate
    );

    // Ingresos por rango de fechas
    @Query("SELECT COALESCE(SUM(r.totalAmount), 0) FROM Reservation r WHERE r.status = 'CONFIRMED' AND r.reservationDate BETWEEN :startDate AND :endDate")
    BigDecimal sumRevenueByDateRange(
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate
    );
}