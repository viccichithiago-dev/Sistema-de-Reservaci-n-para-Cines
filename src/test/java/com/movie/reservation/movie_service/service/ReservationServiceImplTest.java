package com.movie.reservation.movie_service.service;
/**
 * import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
 */
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.movie.reservation.movie_service.config.SeatPriceConfig;
import com.movie.reservation.movie_service.dto.ReservationRequest;
import com.movie.reservation.movie_service.dto.ReservationResponse;
import com.movie.reservation.movie_service.dto.SeatResponse;
import com.movie.reservation.movie_service.dto.ShowtimeResponse;
import com.movie.reservation.movie_service.model.Genre;
import com.movie.reservation.movie_service.model.Movie;
import com.movie.reservation.movie_service.model.Reservation;
import com.movie.reservation.movie_service.model.ReservationStatus;
import com.movie.reservation.movie_service.model.Seat;
import com.movie.reservation.movie_service.model.Showtime;
import com.movie.reservation.movie_service.model.ShowtimeSeat;
import com.movie.reservation.movie_service.model.Theater;
import com.movie.reservation.movie_service.model.User;
import com.movie.reservation.movie_service.repository.ReservationRepository;
import com.movie.reservation.movie_service.repository.ShowtimeRepository;
import com.movie.reservation.movie_service.repository.ShowtimeSeatRepository;
import com.movie.reservation.movie_service.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests para ReservationService")
class ReservationServiceImplTest {

    @Mock
    private UserRepository userRepository;
    
    @Mock
    private ShowtimeRepository showtimeRepository;
    
    @Mock
    private ShowtimeSeatRepository showtimeSeatRepository;
    
    @Mock
    private ReservationRepository reservationRepository;
    
    @Mock
    private SeatPriceConfig seatPriceConfig;
    
    @Mock
    private ReservationMapper reservationMapper;

    @InjectMocks
    private ReservationServiceImpl reservationService;
    // Atributos que van a usar para los tests
    private User userTest;
    private Theater theaterTest;
    private Seat seatTest;
    private Movie movieTest;
    private Theater theaterTestForShowtime;
    private Showtime showtimeTest;
    private ShowtimeSeat showtimeSeatTest;
    private Reservation reservationTest;
    private ReservationRequest validRequest;
    private ShowtimeResponse showtimeResponseTest;
    private SeatResponse seatResponseTest;
    private ReservationResponse reservationResponseTest;

    @BeforeEach
    void setUp(){
        // Inicio de Datos / Clases comunes para los Tests(NECESARIOS)
        userTest = new User("testUser","user@test.com","testPassword");
        userTest.setId(29L);
        theaterTest = new Theater("Test Theater", "Test Location");
        seatTest = new Seat(1L,1,1,theaterTest);
        movieTest = new Movie("Maravillas de Flor","vichi te ama",null,Genre.ROMANCE);
        theaterTestForShowtime = new Theater("Test Theater", "Test Location");
        showtimeTest = new Showtime(
            movieTest,
            theaterTestForShowtime,
            LocalDateTime.now(),
            LocalDateTime.now().plusHours(2)
        );
        showtimeSeatTest = new ShowtimeSeat(showtimeTest,seatTest,false);
        reservationTest = new Reservation(
            userTest,
            LocalDateTime.now(),
            BigDecimal.valueOf(25.0),
            ReservationStatus.PENDING
        );
        reservationTest.getReservedSeats().add(showtimeSeatTest);
        // Request y DTOS de Respuesta
        validRequest = new ReservationRequest(1L,List.of(1L,2L));
        seatResponseTest = new SeatResponse(1L,1,1);
        showtimeResponseTest = new ShowtimeResponse(
            1L,
            1L,
            1L,
            LocalDateTime.now().plusDays(1),
            LocalDateTime.now().plusHours(2));
        reservationResponseTest = new ReservationResponse(
            1L,1L,showtimeResponseTest,
            List.of(seatResponseTest),
            LocalDateTime.now(),
            BigDecimal.valueOf(25.0),
            ReservationStatus.PENDING
        );
    }
    @Nested
    @DisplayName("Escenarios de Creación de Reservas")
    class CreateReservationTests {
        // Test Happy Path
        @Test
        @DisplayName("Debe crear una reserva exitosamente cuando todos los datos son válidos")
        void shouldCreateReservationWhenAllValid(){
            
        }
    }
}