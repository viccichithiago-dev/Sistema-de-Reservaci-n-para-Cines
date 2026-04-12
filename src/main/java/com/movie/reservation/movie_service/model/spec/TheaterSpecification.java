package com.movie.reservation.movie_service.model.spec;
import java.util.Optional;

import org.springframework.data.jpa.domain.Specification;

import com.movie.reservation.movie_service.model.Theater;
public final class TheaterSpecification {
    private TheaterSpecification() {
        // Constructor privado para evitar instanciación
    }
    // Especificación para filtrar por nombre
    public static Specification<Theater> hasName(String name) {
        return (root, query, cb) -> 
        cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }
    
    // Especificación para filtrar por ubicación
    public static Specification<Theater> hasLocation(String location) {
        return (root, query, cb) -> 
        cb.like(cb.lower(root.get("location")), "%" + location.toLowerCase() + "%");
    }

    // Metodo para combinar todos los filtros dinamicamente
    public static Specification<Theater> buildFilters(Optional<String> nameFilter, Optional<String> locationFilter) {
        Specification<Theater> spec = Specification.allOf();

        if (nameFilter.isPresent()) {
            spec = spec.and(hasName(nameFilter.get()));
        }

        if (locationFilter.isPresent()) {
            spec = spec.and(hasLocation(locationFilter.get()));
        }

        return spec;
    }
}
