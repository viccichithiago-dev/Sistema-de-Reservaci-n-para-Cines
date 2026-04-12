package com.movie.reservation.movie_service.service;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.movie.reservation.movie_service.dto.SeatResponse;
import com.movie.reservation.movie_service.dto.TheaterRequest;
import com.movie.reservation.movie_service.dto.TheaterResponse;
import com.movie.reservation.movie_service.exception.TheaterAlreadyExistsException;
import com.movie.reservation.movie_service.exception.TheaterNotFoundException;
import com.movie.reservation.movie_service.model.Seat;
import com.movie.reservation.movie_service.model.Theater;
import com.movie.reservation.movie_service.repository.SeatRepository;
import com.movie.reservation.movie_service.repository.TheaterRepository;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class TheaterServiceImpl implements TheaterService{
    private final TheaterRepository theaterRepository;
    private final SeatRepository seatRepository;

    // Metodo para crear un Teatro
    @Override
    @Transactional
    public TheaterResponse createTheater(TheaterRequest request){
            // Validacion de existencia, evitando duplicacion
            validateDuplicateTheater(request.name(),request.location());
            Theater theater = new Theater(
                request.name(),
                request.location()
            );
            // Guardamos en la DB
            Theater savedTheater = theaterRepository.save(theater);
            return mapToResponse(savedTheater);
    }
    // Metodo para obtener un teatro por su ID
    @Override
    public TheaterResponse getTheaterById(Long id){
        Theater theater = theaterRepository.findById(id)
                .orElseThrow(() -> new TheaterNotFoundException(id));
                return mapToResponse(theater);
    }   
    // Metodo para obtener todos los teatros
    @Override
    public Page<TheaterResponse> getAllTheaters(Pageable pageable,Optional<String> nameFIlter, Optional<String> locationFilter){
        // Validacion
        if(nameFIlter.isPresent() && locationFilter.isPresent()){
            return theaterRepository.findByNameContainingIgnoreCaseAndLocationContainingIgnoreCase(nameFIlter.get(), locationFilter.get(), pageable).map(this::mapToResponse);
        } else if(nameFIlter.isPresent()){
            return theaterRepository.findByNameContainingIgnoreCase(nameFIlter.get(),pageable).map(this::mapToResponse);
        } else if (locationFilter.isPresent()){
            return theaterRepository.findByLocationContainingIgnoreCase(locationFilter.get(), pageable).map(this::mapToResponse);
        } else{
            return theaterRepository.findAll(pageable).map(this::mapToResponse);
        }
    }
    // M
    // Metodos privados para ayudar del Servicio 
    private void validateDuplicateTheater(String name,String location,Long excludeId){
        boolean exists = theaterRepository.existsByNameAndLocation(name, location);
        // Validacion 
        if (exists && excludeId != null) {
        var existingOpt = theaterRepository.findById(excludeId);
        if (existingOpt.isPresent()) {
            exists = exists && !existingOpt.get().getId().equals(excludeId);
        }
        }
        if(exists){
            throw new TheaterAlreadyExistsException(name);
        }
    }
    private void validateDuplicateTheater(String name, String location){
        validateDuplicateTheater(name,location,null);
    }
    // Metodos para actualizar el teatro
    @Override
    @Transactional
    public TheaterResponse updateTheater(Long id, TheaterRequest request){
        Theater theater = theaterRepository.findById(id)
                .orElseThrow(() -> new TheaterNotFoundException(id));
        validateDuplicateTheater(request.name(), request.location(),id);
        theater.setName(request.name());
        theater.setLocation(request.location());
        Theater updateTheater = theaterRepository.save(theater);
        return mapToResponse(updateTheater);
    }
    // Metodo para eliminar un Teatro
    @Override
    @Transactional
    public void deleteTheater(Long id){
        if(!theaterRepository.existsById(id)){
            throw new TheaterNotFoundException(id);
        }
        theaterRepository.deleteById(id);
    }
    // Obtener los asientos por los teatros
    @Override
    public List<SeatResponse> getTheaterSeats(Long theaterId){
        Theater theater = theaterRepository.findById(theaterId)
                .orElseThrow(() -> new TheaterNotFoundException(theaterId));
        
        return seatRepository.findByTheater(theater)
                    .stream()
                    .map(this::seatToResponse)
                    .toList();
    }
    // Metodos de mapeo
    private TheaterResponse mapToResponse(Theater theater){
        return new TheaterResponse(
            theater.getId(),
            theater.getName(),
            theater.getLocation()
        );
    }   
    private SeatResponse seatToResponse(Seat seat){
        return new SeatResponse(
            seat.getId(),
            seat.getSeatNumber(),
            seat.getRow()
        );
    }
}
