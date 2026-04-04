package com.movie.reservation.movie_service.repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.movie.reservation.movie_service.model.Reservation;
import com.movie.reservation.movie_service.model.Seat;
import com.movie.reservation.movie_service.model.Showtime;
import com.movie.reservation.movie_service.model.ShowtimeSeat;
// findByShowtimeIdAndIsBookedFalse(Long showtimeId) (asientos disponibles)
@Repository
public interface ShowtimeSeatRepository extends JpaRepository<ShowtimeSeat, Long>{
    // Metodos esenciales de busqueda por Showtime y Seat
    List<ShowtimeSeat> findByShowtime(Showtime showtime);
    Page<ShowtimeSeat> findByShowtime(Showtime showtime, Pageable pageable);
    Optional<ShowtimeSeat> findByShowtimeAndSeat(Showtime showtime, Seat Seat);
    boolean existsByShowtimeAndSeat(Showtime showtime, Seat seat);

    // Metodos de Estado de Reserva
    List<ShowtimeSeat> findByShowtimeAndIsBookedFalse(Showtime showtime);
    List<ShowtimeSeat> findByShowtimeAndIsBookedTrue(Showtime showtime);
    Page<ShowtimeSeat> findByShowtimeAndIsBookedFalse(Showtime showtime, Pageable pageable);

    // Metodos de reservacion 
    List<ShowtimeSeat> findByReservation(Reservation reservation);
    Page<ShowtimeSeat> findByReservation(Reservation reservation, Pageable pageable);
    boolean existsByReservation(Reservation reservation);
    List<ShowtimeSeat> findByShowtimeIdAndSeatIdIn(Long showtimeId,List<Long> seatsIds);
    List<ShowtimeSeat> findByReservationId(Long reservationId);

    // Metodos de capacidad con consultas JQPL
    // Total de asientos reservados
    @Query("SELECT COUNT(ss) FROM ShowtimeSeat ss WHERE ss.isBooked = true")
    long countBookedSeats();
    // Total de asientos en el sistema
    @Query("SELECT COUNT(ss) FROM ShowtimeSeat ss")
    long countTotalSeats();
    // Asientos reservados por rango de fechas
    @Query("SELECT COUNT(ss) FROM ShowtimeSeat ss WHERE ss.isBooked = true AND ss.reservation.reservationDate BETWEEN :startDate AND :endDate")
    long countBookedSeatsByDateRange(
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate
    );
}
