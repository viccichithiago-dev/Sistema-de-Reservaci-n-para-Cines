package com.movie.reservation.movie_service.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.movie.reservation.movie_service.dto.MoviePopularityResponse;
import com.movie.reservation.movie_service.model.Genre;
import com.movie.reservation.movie_service.model.Movie;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long>, JpaSpecificationExecutor<Movie> {
    // Essential search methods for movie catalog
    // Text searches
    List<Movie> findByTitleContainingIgnoreCase(String title);
    Page<Movie> findAllByTitleContainingIgnoreCase(String title, Pageable pageable);
    
    // Genre searches
    List<Movie> findByGenre(Genre genre);
    List<Movie> findByGenreOrderByTitleAsc(Genre genre);
    Page<Movie> findAllByGenre(Genre genre, Pageable pageable);
    
    // Combined searches
    List<Movie> findByTitleContainingIgnoreCaseAndGenre(String title, Genre genre);
    Page<Movie> findAllByTitleContainingIgnoreCaseAndGenre(String title, Genre genre, Pageable pageable);
    
    // Critical validations
    boolean existsByTitle(String title);
    boolean existsByTitleAndGenre(String title, Genre genre);
    boolean existsById(Long id);
    
    // Utilities 
    void deleteById(Long id);

    // Metodos nuevos para consultas JQPL para obtener peliculas por popularidad
    @Query("""
    SELECT NEW com.movie.reservation.movie_service.dto.MoviePopularityResponse(
        m.id, m.title, m.genre, COUNT(r.id), SUM(r.totalAmount), COUNT(ss.id)
    )
    FROM Movie m
    JOIN Showtime sh ON sh.movie = m
    JOIN ShowtimeSeat ss ON ss.showtime = sh
    JOIN Reservation r ON ss.reservation = r
    WHERE r.status = 'CONFIRMED'
    AND (:startDate IS NULL OR r.reservationDate >= :startDate)
    AND (:endDate IS NULL OR r.reservationDate <= :endDate)
    GROUP BY m.id, m.title, m.genre
    ORDER BY COUNT(r.id) DESC
    """)
    List<MoviePopularityResponse> findMoviesByPopularity(
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate
    );
}