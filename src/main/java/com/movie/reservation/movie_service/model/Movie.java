package com.movie.reservation.movie_service.model;

import org.hibernate.validator.constraints.URL;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "movies")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Size(max=20)
    @Column(nullable=false,length=100)
    private String title;
    @NotBlank
    @Size(max=20)
    @Column()
    private String description;

    @URL
    @Column()
    private String posterUrl;
    
    @Enumerated(EnumType.STRING)
    @Column
    private Genre genre;

    public Movie(String title, String description, String posterUrl, Genre genre){
        this.title = title;
        this.description = description;
        this.posterUrl= posterUrl;
        this.genre=genre;
    }
}
