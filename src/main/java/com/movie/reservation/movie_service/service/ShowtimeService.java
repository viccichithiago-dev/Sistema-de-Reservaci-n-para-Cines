package com.movie.reservation.movie_service.service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.movie.reservation.movie_service.dto.ShowtimeRequest;
import com.movie.reservation.movie_service.dto.ShowtimeResponse;
import com.movie.reservation.movie_service.dto.AvailableSeatResponse;
import com.movie.reservation.movie_service.model.Genre;

public interface ShowtimeService {
    ShowtimeResponse createShowtime(ShowtimeRequest request);
    ShowtimeResponse getShowtimeById(Long id);
    Page<ShowtimeResponse> getAvailableShowtimes(Optional<LocalDate> date, Optional<Genre> genreId,Pageable pageable);
    ShowtimeResponse updateShowtime(Long id, ShowtimeRequest request);
    void deleteShowtime(Long id);
    List<AvailableSeatResponse> getAvailableSeats(Long showtimeId);
}
