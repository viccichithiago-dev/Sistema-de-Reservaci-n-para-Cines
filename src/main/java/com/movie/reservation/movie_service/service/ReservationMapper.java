package com.movie.reservation.movie_service.service;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.movie.reservation.movie_service.dto.ReservationResponse;
import com.movie.reservation.movie_service.dto.SeatResponse;
import com.movie.reservation.movie_service.dto.ShowtimeResponse;
import com.movie.reservation.movie_service.model.Reservation;
import com.movie.reservation.movie_service.model.Showtime;
import com.movie.reservation.movie_service.model.ShowtimeSeat;
@Component
public class ReservationMapper {
    public ReservationResponse toResponse(Reservation reservation) {
        ShowtimeSeat firstSeat = reservation.getReservedSeats().iterator().next();
        Showtime showtime = firstSeat.getShowtime();
        
        return new ReservationResponse(
            reservation.getId(),
            reservation.getUser().getId(),
            new ShowtimeResponse(
                showtime.getId(),
                showtime.getMovie().getId(),
                showtime.getTheater().getId(),
                showtime.getStartTime(),
                showtime.getEndTime()
            ),
            reservation.getReservedSeats().stream()
                .map(seat -> new SeatResponse(
                    seat.getId(),
                    seat.getSeat().getSeatNumber(),
                    seat.getSeat().getRow()
                ))
                .collect(Collectors.toList()),
            reservation.getReservationDate(),
            reservation.getTotalAmount(),
            reservation.getStatus()
        );
    }
}