package com.movie.reservation.movie_service.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
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
@Table(name = "seats",
       uniqueConstraints = {
           @jakarta.persistence.UniqueConstraint(columnNames = {"theater_id", "row", "seatNumber"})
       })
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Min(1)
    @Column(nullable = false)
    private int seatNumber;

    @NotNull
    @Min(1)
    @Column(nullable = false)
    private int row;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "theater_id", nullable = false)
    private Theater theater;
}
