package com.movie.reservation.movie_service.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.movie.reservation.movie_service.model.Theater;


@Repository
public interface TheaterRepository extends JpaRepository<Theater, Long> {
    
    // Búsquedas por nombre (texto)
    Page<Theater> findByNameContainingIgnoreCase(String name,Pageable pageable);
    Page<Theater> findAllByNameContainingIgnoreCase(String name, Pageable pageable);
    
    // Búsquedas por ubicación (texto)
    Page<Theater> findByLocationContainingIgnoreCase(String location,Pageable pageable);
    Page<Theater> findAllByLocationContainingIgnoreCase(String location, Pageable pageable);
    
    // Búsquedas combinadas
    Page<Theater> findByNameContainingIgnoreCaseAndLocationContainingIgnoreCase(String name, String locatio, Pageable pageable);
    
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