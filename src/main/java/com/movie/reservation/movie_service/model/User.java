package com.movie.reservation.movie_service.model;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column()
    private Long id;
    @Pattern(regexp="^[a-zA-Z0-9]+$", message="Error el usuario solo puede contener letras y numeros")
    @Column(nullable = false, unique = true)
    private String username;
    @Email(message = "Error en la direccion de correo")
    @Column(nullable = false, unique = true, length=100)
    private String email;
    @Column(nullable=false, length=100)
    private String password;
    private Role role;

    public User(String username, String email, String password){
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = Role.USER;
    }
}
