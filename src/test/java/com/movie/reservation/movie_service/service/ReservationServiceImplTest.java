package com.movie.reservation.movie_service.service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.movie.reservation.movie_service.config.SeatPriceConfig;
import com.movie.reservation.movie_service.dto.ReservationRequest;
import com.movie.reservation.movie_service.dto.ReservationResponse;
import com.movie.reservation.movie_service.dto.SeatResponse;
import com.movie.reservation.movie_service.dto.ShowtimeResponse;
import com.movie.reservation.movie_service.exception.DuplicateResourceException;
import com.movie.reservation.movie_service.exception.SeatNotFoundException;
import com.movie.reservation.movie_service.exception.ShowtimeNotFoundException;
import com.movie.reservation.movie_service.exception.UserNotFoundException;
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
            // Patron AAA
            // Arrange Configuracion del Comportamiento de los Mocks
            // Act Ejecución del Método a Probar
            // Assert Verificación de Resultados
            // Arrange (Configuracion)
            given(userRepository.findById(29L)).willReturn(java.util.Optional.of(userTest));
            given(showtimeRepository.findById(1L)).willReturn(Optional.of(showtimeTest));
            given(showtimeSeatRepository.findByShowtimeIdAndSeatIdIn(1L, List.of(1L, 2L)))
            .willReturn(List.of(showtimeSeatTest));
            given(seatPriceConfig.calculateTotalPrice(2)).willReturn(25.0);
            given(reservationRepository.save(any(Reservation.class)))
            .willAnswer(invocation -> {
                Reservation res = invocation.getArgument(0);
                res.setId(1L); // Simula el ID generado por la DB
                return res;
            });
            given(reservationMapper.toResponse(any(Reservation.class)))
            .willReturn(reservationResponseTest);
            // Act (Ejecucion del metodo a probar)
            ReservationResponse response = reservationService.createReservation(29L, validRequest);            
            // Assert (Verificacion de Resultados)
            // Verificacion que no sea nulo!
            assertThat(response).isNotNull();
            // Verificacion de que el ID de la reserva sea el esperado
            assertThat(response.id()).isEqualTo(1L);
            // Verificacion de que el ID del usuario sea el esperado
            assertThat(response.userId()).isEqualTo(29L);
            // Verificacion del monto total calculado
            assertThat(response.totalAmount()).isEqualTo(BigDecimal.valueOf(25.0));
            // Verificacion del estado de la reserva
            assertThat(response.status()).isEqualTo(ReservationStatus.PENDING);

            // Verificacion de respuestas
            // Verificamos que la llamada a la base de Datos sea una1 sola vez para cada entidad involucrada
            then(userRepository).should().findById(29L);
            // Verificamos que tambien se haya llamado una sola vez
            then(showtimeRepository).should().findById(1L);
            // Verificacion que se hayan encontrados los asientos correctos
            then(showtimeSeatRepository).should().findByShowtimeIdAndSeatIdIn(1L, List.of(1L, 2L));
            // Verificacion del precio total calculado
            then(seatPriceConfig).should().calculateTotalPrice(2);
            // Verificacion de que se haya guardado la reserva
            then(reservationRepository).should().save(argThat(reservation -> 
                reservation.getUser().equals(userTest) &&
                reservation.getTotalAmount().equals(BigDecimal.valueOf(25.0)) &&
                reservation.getStatus() == ReservationStatus.PENDING
            ));
            // Verificacion que los asientos estan reservados
            then(showtimeSeatRepository).should().saveAll(argThat(
            (Iterable<ShowtimeSeat> seats) -> {
                List<ShowtimeSeat> list = (List<ShowtimeSeat>) seats;
                return list.stream().allMatch(seat -> seat.isBooked());
            }
            ));
            // Verificar que se mappeó la respuesta
            then(reservationMapper).should().toResponse(any(Reservation.class));
        }
        // Test de Errores
        @Test
        @DisplayName("Debe lanzar una excepción cuando el usuario no existe")
        void shouldThrowExceptionWhenUserNotFound(){
            // Arrange
            given(userRepository.findById(29L)).willReturn(Optional.empty());
            // Act & Assert
            assertThatThrownBy(() -> reservationService.createReservation(29L, validRequest))
            .isInstanceOf(UserNotFoundException.class);
            // Verificacion que se haya intentado buscar el usuario
            then(userRepository).should().findById(29L);
            // Nunca deberian ser llamados los repositorios de showtime ni de reserva si el usuario no existe
            then(showtimeRepository).shouldHaveNoInteractions();
            then(reservationRepository).shouldHaveNoInteractions();
        }
        @Test
        @DisplayName("Debe lanzar una excepción cuando el showtime no existe")
        void shouldThrowExceptionWhenShowtimeNotFound(){
            // Arrange
            given(userRepository.findById(29L)).willReturn(Optional.of(userTest));
            given(showtimeRepository.findById(1L)).willReturn(Optional.empty());
            // Act & Assert
            assertThatThrownBy(() -> reservationService.createReservation(29L, validRequest))
            .isInstanceOf(ShowtimeNotFoundException.class);
            // Verificacion que se haya intentado buscar el showtime
            then(showtimeRepository).should().findById(1L);
            // Nunca deberian ser llamados los repositorios de reserva si el showtime no existe
            then(reservationRepository).shouldHaveNoInteractions();
        }
        @Test
        @DisplayName("Debe lanzar una excepcion si hay asientos duplicaddos")
        void shouldThrowExceptionWhenDuplicateSeats(){
            // Arrange
            ReservationRequest requestWithDuplicateSeats = new ReservationRequest(1L, List.of(1L, 1L));
            given(userRepository.findById(29L)).willReturn(Optional.of(userTest));
            given(showtimeRepository.findById(1L)).willReturn(Optional.of(showtimeTest));
            // Act & Assert
            assertThatThrownBy(() -> reservationService.createReservation(29L, requestWithDuplicateSeats))
            .isInstanceOf(DuplicateResourceException.class)
            .hasMessageContaining("Asientos duplicados en la solicitud");
            // Verificacion que nunca se haya llamado al repositorio de asientos ni de reserva debido a la validacion previa de asientos duplicados
            then(showtimeSeatRepository).shouldHaveNoInteractions();
            then(reservationRepository).shouldHaveNoInteractions();
        }
        @Test
        @DisplayName("Asientos solicitados no pertenecen a este showtime")
        void shouldThrowExceptionWhenSeatsDontBelongToShowtime(){
            // Arrange
            ShowtimeSeat bookedSeat = new ShowtimeSeat(showtimeTest, new Seat(2L,1,2,theaterTest), true);
            given(userRepository.findById(29L)).willReturn(Optional.of(userTest));
            given(showtimeRepository.findById(1L)).willReturn(Optional.of(showtimeTest));
            given(showtimeSeatRepository.findByShowtimeIdAndSeatIdIn(1L, List.of(1L, 2L)))
            .willReturn(List.of(bookedSeat));
            // Act & Assert
            assertThatThrownBy(() -> reservationService.createReservation(29L, validRequest))
            .isInstanceOf(SeatNotFoundException.class);
            // Verify que no se haya llamado al repositorio de reserva debido a que los asientos no son válidos para ese showtime
            then(reservationRepository).shouldHaveNoInteractions();
        }
        @Test
        @DisplayName("Debe lanzar una excepcion si hay asientos ya reservados") 
        void shouldThrowExceptionWhenSeatsAlreadyBooked(){
            // Arrange
            ShowtimeSeat bookedSeat = new ShowtimeSeat(showtimeTest, new Seat(1L,1,1,theaterTest), true);
            given(userRepository.findById(29L)).willReturn(Optional.of(userTest));
            given(showtimeRepository.findById(1L)).willReturn(Optional.of(showtimeTest));
            given(showtimeSeatRepository.findByShowtimeIdAndSeatIdIn(1L, List.of(1L, 2L)))
            .willReturn(List.of(bookedSeat));
            // Act & Assert
            assertThatThrownBy(() -> reservationService.createReservation(29L, validRequest))
            .isInstanceOf(SeatNotFoundException.class)
            .hasMessageContaining("Asientos ya reservados");
            // Verify que no se haya llamado al repositorio de reserva debido a que los asientos ya están reservados
            then(reservationRepository).shouldHaveNoInteractions();
        }
    }
}