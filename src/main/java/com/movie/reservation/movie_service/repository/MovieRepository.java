package com.movie.reservation.movie_service.repository;
import com.movie.reservation.movie_service.model.Movie;
import com.movie.reservation.movie_service.model.Genre;
import org.springframework.data.domain  .Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;
@Repository
public interface MovieRepository extends JpaRepository<Movie, Long>{
    // Metodos de busqueda esenciales para catalogo de peliculas
    // Busquedas de texto
    List<Movie> findByTitleContainingIgnoreCase(String title);
    Page<Movie> findAllByTitleContainingIgnoreCase(String title, Pageable pageable);
    
    // Busquedas por genero
    List<Movie> findByGenre(Genre genre);
    List<Movie> findByGenreOrderByTitleAsc(Genre genre);
    Page<Movie> findAllByGenre(Genre genre, Pageable pageable);
    
    // Busquedas combinadas
    List<Movie> findByTitleContainingIgnoreCaseAndGenre(String title, Genre genre);

    // Validaciones criticas
    boolean existsByTitle(String title);
    boolean existsByTitleAndGenre(String title, Genre genre);
    boolean existsById(Long id);
    
    //Utilidades 
    void deleteById(Long id);
}
