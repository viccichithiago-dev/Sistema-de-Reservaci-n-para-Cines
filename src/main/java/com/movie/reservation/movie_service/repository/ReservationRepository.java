package com.movie.reservation.movie_service.repository;

import com.movie.reservation.movie_service.model.Reservation;
<<<<<<< HEAD
import com.movie.reservation.movie_service.model.User;
=======
>>>>>>> 4dd8677 (Implementacion del Repository para Reservation con sus respectivos metodos a implementar en el service)
import com.movie.reservation.movie_service.model.ReservationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
<<<<<<< HEAD
import java.util.Optional;
=======
>>>>>>> 4dd8677 (Implementacion del Repository para Reservation con sus respectivos metodos a implementar en el service)

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    
    // Find by user
    List<Reservation> findByUserId(Long userId);
    Page<Reservation> findByUserId(Long userId, Pageable pageable);
    List<Reservation> findByUserIdOrderByReservationDateDesc(Long userId);
    
<<<<<<< HEAD
    // Find by date range (REQUESTED)
=======
    // Find by date range
>>>>>>> 4dd8677 (Implementacion del Repository para Reservation con sus respectivos metodos a implementar en el service)
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
    
<<<<<<< HEAD
    // Standard pagination and sorting
=======
    // Standard pagination and sorting (explicit for clarity, though inherited)
>>>>>>> 4dd8677 (Implementacion del Repository para Reservation con sus respectivos metodos a implementar en el service)
    Page<Reservation> findAll(Pageable pageable);
    List<Reservation> findAllByOrderByReservationDateDesc();
    
    // Existence checks
    boolean existsByUserId(Long userId);
    boolean existsById(Long id);
}