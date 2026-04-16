package com.movie.reservation.movie_service.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final GenreConverter genreConverter;

    public WebConfig(GenreConverter genreConverter) {
        this.genreConverter = genreConverter;
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        // Registramos nuestro convertidor de géneros
        registry.addConverter(genreConverter);
    }
}