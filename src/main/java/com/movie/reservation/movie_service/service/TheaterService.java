package com.movie.reservation.movie_service.service;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.movie.reservation.movie_service.dto.SeatResponse;
import com.movie.reservation.movie_service.dto.TheaterRequest;
import com.movie.reservation.movie_service.dto.TheaterResponse;
public interface TheaterService {
    // Operaciones CRUD
    // Crear
    TheaterResponse createTheater(TheaterRequest request);
    // Obtener
    TheaterResponse getTheaterById(Long id);
    Page<TheaterResponse> getAllTheaters(Pageable pageable, Optional<String> nameFilter, Optional<String> locationFilter);
    // Actualizar
    TheaterResponse updateTheater(Long id,TheaterRequest request);
    // Eliminar
    void  deleteTheater(Long id);
    // Obtener los asientos de los Teatros
    List<SeatResponse> getTheaterSeats(Long theaterId);
}
