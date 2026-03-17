package com.movie.reservation.movie_service.repository;
import com.movie.reservation.movie_service.model.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
//  findByMovieIdAndStartTimeAfter(Long movieId, LocalDateTime startTime),
//  findByStartTimeBetween(LocalDateTime from, LocalDateTime to)

@Repository
public interface ShowtimeRepository extends JpaRepository<Showtime, Long> {
    // Busquedas esenciales por movie y theater
    List<Showtime> findByMovie(Movie movie);
    Page<Showtime> findByMovie(Movie movie,Pageable pageable);
    List<Showtime> findByTheater(Theater theater);
    Page<Showtime> findByTheater(Theater theater, Pageable pageable);
    List<Showtime> findByMovieAndTheater(Movie movie, Theater theater);
    Page<Showtime> findByMovieAndTheater(Movie movie, Theater theater,Pageable pageable);

    // Busquedas por rango de tiempo (criticas por disponibilidad)
    List<Showtime> findByStartTimeBetween(LocalDateTime start, LocalDateTime end);
    Page<Showtime> findByStartTimeBetween(LocalDateTime start,LocalDateTime end,Pageable pageable);
    List<Showtime> findByMovieAndStartTimeBetween(Movie movie,LocalDateTime start,LocalDateTime end);
    List<Showtime> findByTheaterAndStartTimeBetween(Theater theater, LocalDateTime start, LocalDateTime end);
    
    // Validaciones basicas
    boolean existsByMovieAndTheaterAndStartTime(Movie movie,Theater theater, LocalDateTime startTime);

    // Ordenacion estandar
    List<Showtime> findByTheaterOrderByStartTimeAsc(Theater theater);
    List<Showtime> findByMovieOrderByStartTimeAsc(Movie movie);
    Page<Showtime> findAllByOrderByStartTimeAsc(Pageable pageable);

    // Utilidades
    boolean existsById(Long id);
}
