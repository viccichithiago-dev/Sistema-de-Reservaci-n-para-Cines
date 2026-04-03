package com.movie.reservation.movie_service.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.movie.reservation.movie_service.dto.ReservationRequest;
import com.movie.reservation.movie_service.dto.ReservationResponse;
public interface ReservationService {
    ReservationResponse createReservation(Long userId, ReservationRequest request);
    Page<ReservationResponse> getUserReservations(Long userId,Pageable page);
    ReservationResponse getReservationById(Long id);
    ReservationResponse cancelReservation(Long reservationId);
}
