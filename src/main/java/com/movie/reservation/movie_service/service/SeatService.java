package com.movie.reservation.movie_service.service;
import java.util.List;
import com.movie.reservation.movie_service.dto.SeatResponse;
public interface SeatService {
    SeatResponse getSeatById(Long id);
    List<SeatResponse> getSeatsByTheater(Long theaterId);
}
