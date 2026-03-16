package com.movie.reservation.movie_service.repository;

import com.movie.reservation.movie_service.model.Reservation;
import com.movie.reservation.movie_service.model.User;
import com.movie.reservation.movie_service.model.ReservationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
}