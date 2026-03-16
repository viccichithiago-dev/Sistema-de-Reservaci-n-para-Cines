package com.movie.reservation.movie_service.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "showtime_seats",
       indexes = {
           @Index(name = "uk_showtime_seat", columnList = "showtime_id, seat_id", unique = true),
           @Index(name = "idx_showtime_seat_reservation", columnList = "reservation_id")
       })
public class ShowtimeSeat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "showtime_id", nullable = false)
    private Showtime showtime;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id", nullable = false)
    private Seat seat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @NotNull
    @Column(nullable = false)
    private boolean isBooked;

    // Constructor for creating new showtime seat
    public ShowtimeSeat(Showtime showtime, Seat seat, boolean isBooked) {
        this.showtime = showtime;
        this.seat = seat;
        this.isBooked = isBooked;
    }

    // Constructor for creating new showtime seat with reservation
    public ShowtimeSeat(Showtime showtime, Seat seat, Reservation reservation, boolean isBooked) {
        this.showtime = showtime;
        this.seat = seat;
        this.reservation = reservation;
        this.isBooked = isBooked;
    }
}