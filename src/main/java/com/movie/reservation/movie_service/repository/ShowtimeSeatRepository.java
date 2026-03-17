package com.movie.reservation.movie_service.repository;
import com.movie.reservation.movie_service.model.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
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
}
