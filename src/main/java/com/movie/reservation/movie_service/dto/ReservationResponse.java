package com.movie.reservation.movie_service.dto;

import com.movie.reservation.movie_service.model.ReservationStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record ReservationResponse(
    Long id,
    Long userId,
    ShowtimeResponse showtimeDetails,
    List<SeatResponse> seats,
    LocalDateTime reservationDate,
    BigDecimal totalAmount,
    ReservationStatus status
) {}