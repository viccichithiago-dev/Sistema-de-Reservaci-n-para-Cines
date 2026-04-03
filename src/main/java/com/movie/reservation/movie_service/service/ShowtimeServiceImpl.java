package com.movie.reservation.movie_service.service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.movie.reservation.movie_service.dto.ShowtimeRequest;
import com.movie.reservation.movie_service.dto.ShowtimeResponse;
import com.movie.reservation.movie_service.model.Genre;
import com.movie.reservation.movie_service.model.Movie;
import com.movie.reservation.movie_service.model.Showtime;
import com.movie.reservation.movie_service.model.Theater;
import com.movie.reservation.movie_service.repository.MovieRepository;
import com.movie.reservation.movie_service.repository.ShowtimeRepository;
import com.movie.reservation.movie_service.repository.TheaterRepository;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class ShowtimeServiceImpl implements ShowtimeService {
    private final ShowtimeRepository showtimeRepository;
    private final MovieRepository movieRepository;
    private final TheaterRepository theaterRepository;
    @Override
    @Transactional
    public ShowtimeResponse createShowtime(ShowtimeRequest request) {
        // Validar que la película y el teatro existan
        Movie movie = movieRepository.findById(request.movieId())
                .orElseThrow(() -> new RuntimeException("Película no encontrada con ID: " + request.movieId()));
        Theater theater = theaterRepository.findById(request.theaterId())
                .orElseThrow(() -> new RuntimeException("Teatro no encontrado con ID: " + request.theaterId()));
        
        // Validar que no haya solapamiento de horarios en el mismo teatro
        validateNoOverlap(theater, request.startTime(), request.endTime(), null);
        
        // Validar que startTime sea antes de endTime
        if (!request.startTime().isBefore(request.endTime())) {
            throw new RuntimeException("La hora de inicio debe ser anterior a la hora de fin");
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
                .orElseThrow(() -> new RuntimeException("Showtime no encontrado con ID: " + id));
        return mapToResponse(showtime);
    }
    @Override
    public List<ShowtimeResponse> getAvailableShowtimes(LocalDate date, Optional<Genre> genre) {
        // Definir rango de fechas para el día especificado (00:00:00 a 23:59:59)
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23, 59, 59, 999999999);
        
        List<Showtime> showtimes;
        if (genre.isPresent()) {
            // Filtrar por fecha y género de película
            showtimes = showtimeRepository.findByStartTimeBetween(startOfDay, endOfDay);
            showtimes = showtimes.stream()
            .filter(st -> st.getMovie().getGenre() == genre.get())
            .toList();
        } else {
            // Filtrar solo por fecha
            showtimes = showtimeRepository.findByStartTimeBetween(startOfDay, endOfDay);
        }
        
        // Ordenar por hora de inicio
        showtimes.sort((s1, s2) -> s1.getStartTime().compareTo(s2.getStartTime()));
        
        return showtimes.stream()
                .map(this::mapToResponse)
                .toList();
    }
    @Override
    @Transactional
    public ShowtimeResponse updateShowtime(Long id, ShowtimeRequest request) {
        Showtime showtime = showtimeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Showtime no encontrado con ID: " + id));
        
        // Validar que película y teatro existan (si se están actualizando)
        Movie movie = movieRepository.findById(request.movieId())
                .orElseThrow(() -> new RuntimeException("Película no encontrada con ID: " + request.movieId()));
        Theater theater = theaterRepository.findById(request.theaterId())
                .orElseThrow(() -> new RuntimeException("Teatro no encontrado con ID: " + request.theaterId()));
        
        // Validar que no haya solapamiento con otros showtimes en el mismo teatro (excluyendo el actual)
        validateNoOverlap(theater, request.startTime(), request.endTime(), id);
        
        // Validar que startTime sea antes de endTime
        if (!request.startTime().isBefore(request.endTime())) {
            throw new RuntimeException("La hora de inicio debe ser anterior a la hora de fin");
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
            throw new RuntimeException("Showtime no encontrado con ID: " + id);
        }
        showtimeRepository.deleteById(id);
    }
    // Métodos privados de ayuda
    private void validateNoOverlap(Theater theater, LocalDateTime newStart, LocalDateTime newEnd, Long excludeId) {
        // Para una implementación completa, necesitaríamos un método en el repositorio que encuentre showtimes solapados
        // Como aproximación, obtenemos todos los showtimes del teatro y verificamos solapamiento en memoria
        List<Showtime> theaterShowtimes = showtimeRepository.findByTheater(theater);
        
        for (Showtime existing : theaterShowtimes) {
            // Excluir el showtime actual si estamos actualizando
            if (excludeId != null && existing.getId().equals(excludeId)) {
                continue;
            }
            
            // Verificar solapamiento: [newStart, newEnd) y [existingStart, existingEnd)
            boolean overlaps = newStart.isBefore(existing.getEndTime()) && 
                              newEnd.isAfter(existing.getStartTime());
            
            if (overlaps) {
                throw new RuntimeException("El horario se solapa con otro showtime en el mismo teatro");
            }
        }
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