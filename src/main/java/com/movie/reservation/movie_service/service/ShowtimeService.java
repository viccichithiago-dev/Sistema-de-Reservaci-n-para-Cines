package com.movie.reservation.movie_service.service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.movie.reservation.movie_service.dto.ShowtimeRequest;
import com.movie.reservation.movie_service.dto.ShowtimeResponse;
import com.movie.reservation.movie_service.model.Genre;

public interface ShowtimeService {
    ShowtimeResponse createShowtime(ShowtimeRequest request);
    ShowtimeResponse getShowtimeById(Long id);
    List<ShowtimeResponse> getAvailableShowtimes(LocalDate date, Optional<Genre> genreId);
    ShowtimeResponse updateShowtime(Long id, ShowtimeRequest request);
    void deleteShowtime(Long id);
}
