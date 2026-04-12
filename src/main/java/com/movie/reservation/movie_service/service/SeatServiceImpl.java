package com.movie.reservation.movie_service.service;
import java.util.List;

import org.springframework.stereotype.Service;

import com.movie.reservation.movie_service.dto.SeatResponse;
import com.movie.reservation.movie_service.exception.SeatNotFoundException;
import com.movie.reservation.movie_service.model.Seat;
import com.movie.reservation.movie_service.repository.SeatRepository;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class SeatServiceImpl implements SeatService{
    private final SeatRepository seatRepository;

    // Obtener asiento por su ID
    @Override
    public SeatResponse getSeatById(Long id){
        Seat seat = seatRepository.findById(id)
                .orElseThrow(() -> new SeatNotFoundException(id));
        return seatToResponse(seat);
    }
    // Lista de los asientos del teatro
    @Override
    public List<SeatResponse> getSeatsByTheater(Long theaterId){
        List<Seat> seats = seatRepository.findByTheaterId(theaterId);
        return seats.stream()
                .map(this::seatToResponse)
                .toList();
    }
    // Metodo de Mapeo
    private SeatResponse seatToResponse(Seat seat){
        return new SeatResponse(
            seat.getId(),
            seat.getSeatNumber(),
            seat.getRow()
        );
    } 
}
