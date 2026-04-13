package com.movie.reservation.movie_service.service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.movie.reservation.movie_service.config.SeatPriceConfig;
import com.movie.reservation.movie_service.dto.AvailableSeatResponse;
import com.movie.reservation.movie_service.dto.ShowtimeRequest;
import com.movie.reservation.movie_service.dto.ShowtimeResponse;
import com.movie.reservation.movie_service.exception.InvalidShowtimeTimeException;
import com.movie.reservation.movie_service.exception.MovieNotFoundException;
import com.movie.reservation.movie_service.exception.ShowtimeNotFoundException;
import com.movie.reservation.movie_service.exception.ShowtimeOverlapException;
import com.movie.reservation.movie_service.exception.TheaterNotFoundException;
import com.movie.reservation.movie_service.model.Genre;
import com.movie.reservation.movie_service.model.Movie;
import com.movie.reservation.movie_service.model.Showtime;
import com.movie.reservation.movie_service.model.ShowtimeSeat;
import com.movie.reservation.movie_service.model.Theater;
import com.movie.reservation.movie_service.repository.MovieRepository;
import com.movie.reservation.movie_service.repository.ShowtimeRepository;
import com.movie.reservation.movie_service.repository.ShowtimeSeatRepository;
import com.movie.reservation.movie_service.repository.TheaterRepository;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class ShowtimeServiceImpl implements ShowtimeService {
    private final ShowtimeRepository showtimeRepository;
    private final MovieRepository movieRepository;
    private final TheaterRepository theaterRepository;
    private final ShowtimeSeatRepository showtimeSeatRepository;
    private final SeatPriceConfig seatPriceConfig;
    @Override
    @Transactional
    public ShowtimeResponse createShowtime(ShowtimeRequest request) {
        // Validar que la película y el teatro existan
        Movie movie = movieRepository.findById(request.movieId())
                .orElseThrow(() -> new MovieNotFoundException(request.movieId()));
        Theater theater = theaterRepository.findById(request.theaterId())
                .orElseThrow(() -> new TheaterNotFoundException(request.theaterId()));
        
        // Validar que no haya solapamiento de horarios en el mismo teatro
        validateNoOverlap(theater, request.startTime(), request.endTime(), null);
        
        // Validar que startTime sea antes de endTime
        if (!request.startTime().isBefore(request.endTime())) {
            throw new InvalidShowtimeTimeException(theater.getId());
        }
        
        // Crear y guardar showtime
        Showtime showtime = new Showtime(
                movie,
                theater,
                request.startTime(),
                request.endTime()
        );
        
        Showtime savedShowtime = showtimeRepository.save(showtime);
        return mapToResponse(savedShowtime);
    }
    @Override
    public ShowtimeResponse getShowtimeById(Long id) {
        Showtime showtime = showtimeRepository.findById(id)
                .orElseThrow(() -> new ShowtimeNotFoundException(id));
        return mapToResponse(showtime);
    }
    @Override
    public Page<ShowtimeResponse> getAvailableShowtimes(Optional<LocalDate> date, Optional<Genre> genre, Pageable pageable) {
        LocalDate targetDate = date.orElse(LocalDate.now());
        LocalDateTime startOfDay = targetDate.atStartOfDay();
        LocalDateTime endOfDay = targetDate.atTime(23,59,59,9999999);
        if(genre.isPresent()){
            return showtimeRepository
                .findByStartTimeBetweenAndMovieGenre(startOfDay, endOfDay, genre.get(), pageable)
                .map(this::mapToResponse);
        } else{
            return showtimeRepository
                .findByStartTimeBetween(startOfDay, endOfDay, pageable)
                .map(this::mapToResponse);
        }
    }
    @Override
    @Transactional
    public ShowtimeResponse updateShowtime(Long id, ShowtimeRequest request) {
        Showtime showtime = showtimeRepository.findById(id)
                .orElseThrow(() -> new ShowtimeNotFoundException(id));
        
        // Validar que película y teatro existan (si se están actualizando)
        Movie movie = movieRepository.findById(request.movieId())
                .orElseThrow(() -> new MovieNotFoundException(request.movieId()));
        Theater theater = theaterRepository.findById(request.theaterId())
                .orElseThrow(() -> new TheaterNotFoundException(request.theaterId()));
        
        // Validar que no haya solapamiento con otros showtimes en el mismo teatro (excluyendo el actual)
        validateNoOverlap(theater, request.startTime(), request.endTime(), id);
        
        // Validar que startTime sea antes de endTime
        if (!request.startTime().isBefore(request.endTime())) {
            throw new InvalidShowtimeTimeException(id);
        }
        
        // Actualizar showtime
        showtime.setMovie(movie);
        showtime.setTheater(theater);
        showtime.setStartTime(request.startTime());
        showtime.setEndTime(request.endTime());
        
        Showtime updatedShowtime = showtimeRepository.save(showtime);
        return mapToResponse(updatedShowtime);
    }
    @Override
    @Transactional
    public void deleteShowtime(Long id) {
        if (!showtimeRepository.existsById(id)) {
            throw new ShowtimeNotFoundException(id);
        }
        showtimeRepository.deleteById(id);
    }
    // Métodos privados de ayuda
    private void validateNoOverlap(Theater theater, LocalDateTime newStart, LocalDateTime newEnd, Long excludeId) {
        // Para una implementación completa, necesitaríamos un método en el repositorio que encuentre showtimes solapados
        // Como aproximación, obtenemos todos los showtimes del teatro y verificamos solapamiento en memoria
        List<Showtime> overlappingShowtimes = showtimeRepository.findOverlappingShowtimes(theater, newStart, newEnd, excludeId);
        if (!overlappingShowtimes.isEmpty()) {
            throw new ShowtimeOverlapException(overlappingShowtimes.get(0).getId());
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<AvailableSeatResponse> getAvailableSeats(Long showtimeId) {
        Showtime showtime = showtimeRepository.findById(showtimeId)
                .orElseThrow(() -> new ShowtimeNotFoundException(showtimeId));
        List<ShowtimeSeat> allSeats = showtimeSeatRepository.findByShowtime(showtime); 
        return allSeats.stream()
                .map(ss -> new AvailableSeatResponse(
                        ss.getId(),
                        ss.getSeat().getId(),
                        ss.getSeat().getSeatNumber(),
                        ss.getSeat().getRow(),
                        ss.isBooked(),
                        seatPriceConfig.getStandardPrice()
                ))
                .toList();
    
    } 
    private ShowtimeResponse mapToResponse(Showtime showtime) {
        return new ShowtimeResponse(
                showtime.getId(),
                showtime.getMovie().getId(),
                showtime.getTheater().getId(),
                showtime.getStartTime(),
                showtime.getEndTime()
        );
    }
}