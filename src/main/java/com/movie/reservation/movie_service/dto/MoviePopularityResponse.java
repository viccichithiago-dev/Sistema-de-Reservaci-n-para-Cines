package com.movie.reservation.movie_service.dto;
import java.math.BigDecimal;

import com.movie.reservation.movie_service.model.Genre;
public record MoviePopularityResponse(
    Long movieId,
    String movieTitle,
    Genre genre,
    long totalReservations,
    BigDecimal totalRevenue,
    long totalSeatsBooked
) {
    // Contructor que maneja nulos
    public MoviePopularityResponse{
        if(totalRevenue == null){
            totalRevenue = BigDecimal.ZERO;
        }
    }
}
