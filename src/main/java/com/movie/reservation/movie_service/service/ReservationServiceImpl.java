package com.movie.reservation.movie_service.service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.movie.reservation.movie_service.dto.ReservationRequest;
import com.movie.reservation.movie_service.dto.ReservationResponse;
import com.movie.reservation.movie_service.exception.InvalidCancellationException;
import com.movie.reservation.movie_service.exception.ReservationAlreadyCancelledException;
import com.movie.reservation.movie_service.exception.ReservationNotFoundException;
import com.movie.reservation.movie_service.exception.SeatsAlreadyBookedException;
import com.movie.reservation.movie_service.exception.SeatsNotBelongingToShowtimeException;
import com.movie.reservation.movie_service.exception.ShowtimeNotFoundException;
import com.movie.reservation.movie_service.exception.UserNotFoundException;
import com.movie.reservation.movie_service.model.Reservation;
import com.movie.reservation.movie_service.model.ReservationStatus;
import com.movie.reservation.movie_service.model.Showtime;
import com.movie.reservation.movie_service.model.ShowtimeSeat;
import com.movie.reservation.movie_service.model.User;
import com.movie.reservation.movie_service.repository.ReservationRepository;
import com.movie.reservation.movie_service.repository.ShowtimeRepository;
import com.movie.reservation.movie_service.repository.ShowtimeSeatRepository;
import com.movie.reservation.movie_service.repository.UserRepository;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService{
    private final ReservationRepository reservationRepository;
    private final ShowtimeRepository showtimeRepository;
    private final ShowtimeSeatRepository showtimeSeatRepository;
    private final UserRepository userRepository;
    private final ReservationMapper reservationMapper;

    // Metodo para crear una reserva
    @Override
    @Transactional
    public ReservationResponse createReservation(Long userId, ReservationRequest request){
        // Validaciones
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        Showtime showtime = showtimeRepository.findById(request.showtimeId())
                .orElseThrow(() -> new ShowtimeNotFoundException(request.showtimeId()));      
        List<Long> seatIds = request.seatIds();
        List<ShowtimeSeat> seats = showtimeSeatRepository.findByShowtimeIdAndSeatIdIn(request.showtimeId(), seatIds);
        if(seats.size() != seatIds.size()){
            throw new SeatsNotBelongingToShowtimeException(request.showtimeId());
        }
        if(seats.stream().anyMatch(ShowtimeSeat::isBooked)){
            throw new SeatsAlreadyBookedException(request.showtimeId());
        }
        // Calcular el total para la reserva de los asientos!
        double pricePerSeat= 10.0;
        double totalAmount = pricePerSeat * seatIds.size();
        // Creacion de la reserva
        Reservation reservation = new Reservation(
            user,
            LocalDateTime.now(),
            BigDecimal.valueOf(totalAmount),
            ReservationStatus.CONFIRMED
        );
        // Guardar reservacion en la DB
        Reservation savedReservation = reservationRepository.save(reservation);

        // Marcarmos los asientos com oreservados
        seats.forEach(seat -> {
            seat.setBooked(true);
            seat.setReservation(savedReservation);
        });
        showtimeSeatRepository.saveAll(seats);
        // Mapeo de la reservacion
        return reservationMapper.toResponse(savedReservation);
    }
    // Metodo para obtener las reservaciones de un usuario
    @Override
    public Page<ReservationResponse> getUserReservations(Long userId,Pageable pageable){
        // Validamos la existencia del usuario
        if(!userRepository.existsById(userId)){
            throw new UserNotFoundException(userId);
        }
        Page<Reservation> reservations = reservationRepository.findByUserId(userId, pageable);
        return reservations.map(reservationMapper::toResponse);
    }
    // Metodo para obtener una reservacion por el ID
    @Override
    public ReservationResponse getReservationById(Long id){
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException(id));
        return reservationMapper.toResponse(reservation);
    }
    // Metodo para cancelar una reservacion!
    @Override
    @Transactional
    public ReservationResponse cancelReservation(Long reservationId){
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException(reservationId));
        // Validaciones antes de cancelar la reserva
        // Verificamos que no cancele una reserva el mismo dia
        if(reservation.getReservationDate().isBefore(LocalDateTime.now())){
            throw new InvalidCancellationException(reservationId);
        }
        // Verificamos que no pueda cancelar una reserva ya antes cancelada o ya que este completa
        if(reservation.getStatus()== ReservationStatus.CANCELLED || reservation.getStatus() == ReservationStatus.COMPLETED){
            throw new ReservationAlreadyCancelledException(reservationId);
        }
        // Liberamos los asientos
        List<ShowtimeSeat> reservedSeats = showtimeSeatRepository.findByReservationId(reservationId);
        reservedSeats.forEach(seat ->{
            seat.setReservation(null);
            seat.setBooked(false);
        });
        showtimeSeatRepository.saveAll(reservedSeats);

        // Actualizar estado de la reserva!
        reservation.setStatus(ReservationStatus.CANCELLED);
        Reservation updateReservation = reservationRepository.save(reservation);
        
        return reservationMapper.toResponse(updateReservation);
    }
}
