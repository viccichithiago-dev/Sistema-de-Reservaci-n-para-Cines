package com.movie.reservation.movie_service.model.spec;
import org.springframework.data.jpa.domain.Specification;

import com.movie.reservation.movie_service.model.Genre;
import com.movie.reservation.movie_service.model.Movie;
public final class MovieSpecification {
    private MovieSpecification() {
        // Constructor privado para evitar instanciación
    }
    // Especificación para filtrar por género
    public static Specification<Movie> hasGenre(Genre genre) {
       return(root, query, cb) -> cb.equal(root.get("genre"), genre);
    }
    // Especificación para filtrar por título
    public static Specification<Movie> hasTitle(String title) {
        return(root, query, cb) -> cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%");
    }
}
