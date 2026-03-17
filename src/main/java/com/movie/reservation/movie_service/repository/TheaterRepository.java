package com.movie.reservation.movie_service.repository;

import com.movie.reservation.movie_service.model.Theater;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface TheaterRepository extends JpaRepository<Theater, Long> {
    
    // Búsquedas por nombre (texto)
    List<Theater> findByNameContainingIgnoreCase(String name);
    Page<Theater> findAllByNameContainingIgnoreCase(String name, Pageable pageable);
    
    // Búsquedas por ubicación (texto)
    List<Theater> findByLocationContainingIgnoreCase(String location);
    Page<Theater> findAllByLocationContainingIgnoreCase(String location, Pageable pageable);
    
    // Búsquedas combinadas
    List<Theater> findByNameContainingIgnoreCaseAndLocationContainingIgnoreCase(String name, String location);
    
    // Validaciones críticas para evitar duplicados
    boolean existsByName(String name);
    boolean existsByLocation(String location);
    boolean existsByNameAndLocation(String name, String location);
    
    // Soporte para paginación y ordenamiento estándar
    Page<Theater> findAll(Pageable pageable);
    List<Theater> findAllByOrderByNameAsc();
    List<Theater> findAllByOrderByLocationAsc();
    
    // Utilidades
    boolean existsById(Long id);
    void deleteById(Long id);
}