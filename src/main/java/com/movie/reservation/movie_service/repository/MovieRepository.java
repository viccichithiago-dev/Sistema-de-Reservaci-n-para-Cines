package com.movie.reservation.movie_service.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.movie.reservation.movie_service.model.Genre;
import com.movie.reservation.movie_service.model.Movie;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long>{
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
}