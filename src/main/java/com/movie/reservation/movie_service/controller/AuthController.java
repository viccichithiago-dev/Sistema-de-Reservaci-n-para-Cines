package com.movie.reservation.movie_service.controller;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.movie.reservation.movie_service.dto.AuthResponse;
import com.movie.reservation.movie_service.dto.LoginRequest;
import com.movie.reservation.movie_service.dto.UserDTO;
import com.movie.reservation.movie_service.dto.UserRegistrationRequest;
import com.movie.reservation.movie_service.service.UserService;
import com.movie.reservation.movie_service.dto.AuthResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    // Endpoint de registro del usuario
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody UserRegistrationRequest request){
        UserDTO user = userService.registerUser(request);
        AuthResult auth = userService.authenticateUser(new LoginRequest(user.email(), request.password()));
        AuthResponse response = new AuthResponse(
            auth.token(),
            user.email(),
            auth.role().name(),
            86400
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    // Endpoint de login del usuario
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request){
        AuthResult auth = userService.authenticateUser(request);
        AuthResponse response = new AuthResponse(
            auth.token(),
            auth.email(),
            auth.role().name(),
            86400
        );
        return ResponseEntity.ok(response);
    }
}
