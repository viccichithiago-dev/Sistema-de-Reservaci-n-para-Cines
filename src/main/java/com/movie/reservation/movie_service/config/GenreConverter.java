package com.movie.reservation.movie_service.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import com.movie.reservation.movie_service.model.Genre;

@Component
public class GenreConverter implements Converter<String, Genre> {

    @Override
    public Genre convert(@NonNull String source) {
        try {
            // Convierte el texto de la URL a MAYÚSCULAS para que coincida con el Enum
            return Genre.valueOf(source.toUpperCase().trim());
        } catch (IllegalArgumentException e) {
            // Si el texto no coincide con ningún género, devolvemos null
            // Esto permitirá que el ExceptionHandler que creamos antes maneje el error limpiamente
            return null; 
        }
    }
}