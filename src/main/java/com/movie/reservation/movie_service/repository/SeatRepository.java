package com.movie.reservation.movie_service.repository;

import com.movie.reservation.movie_service.model.Seat;
import com.movie.reservation.movie_service.model.Theater;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long>{
    // Busquedas esenciales por teatro
    List<Seat> findByTheater(Theater theater);
    Page<Seat> findByTheater(Theater theater, Pageable pageable);
    Optional<Seat> findByTheaterAndRowAndSeatNumber(Theater theater,int row,int seatNumber);

    // Busquedas utiles
    List<Seat> findByTheaterAndRow(Theater theater, int row);
    List<Seat> findByTheaterAndSeatNumberBetween(Theater theater, int start, int end);
    List<Seat> findByTheaterOrderByRowAscSeatNumberAsc(Theater theater);
    // Soporte para paginación y ordenamiento estándar
    Page<Seat> findAll(Pageable pageable);
    
    // Validaciones
    boolean existsById(Long id);
    void deleteById(Long id);
    boolean existsByTheaterAndRowAndSeatNumber(Theater theater, int row, int seatNumber);
}
