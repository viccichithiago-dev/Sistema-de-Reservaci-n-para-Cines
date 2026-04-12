package com.movie.reservation.movie_service.service;

import java.util.List;

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
        List<ShowtimeSeat> reservedSeats = reservation.getReservedSeats();
        // Manejo defensivo de una colecion vacia de asientos reservados
        if (reservedSeats == null || reservedSeats.isEmpty()) {
            throw new IllegalStateException(
                String.format("La reserva con ID %d no tiene asientos reservados", reservation.getId())
            );
        }
        Showtime showtime = reservedSeats.get(0).getShowtime();
        
        List<SeatResponse> seatResponses = reservedSeats.stream()
            .map(this::mapToSeatResponse)
            .toList();
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
            seatResponses,
            reservation.getReservationDate(),
            reservation.getTotalAmount(),
            reservation.getStatus()
        );
    }
    private SeatResponse mapToSeatResponse(ShowtimeSeat showtimeSeat) {
        return new SeatResponse(
            showtimeSeat.getSeat().getId(),
            showtimeSeat.getSeat().getSeatNumber(),
            showtimeSeat.getSeat().getRow()
        );
    }
}