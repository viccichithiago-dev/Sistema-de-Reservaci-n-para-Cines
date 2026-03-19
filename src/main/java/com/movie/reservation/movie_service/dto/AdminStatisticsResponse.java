package com.movie.reservation.movie_service.dto;

import java.math.BigDecimal;
import java.util.List;

public record AdminStatisticsResponse(
    long totalReservations,
    BigDecimal totalRevenue,
    double occupancyRate,
    List<MovieResponse> popularMovies
) {}