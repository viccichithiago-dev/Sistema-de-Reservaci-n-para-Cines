package com.movie.reservation.movie_service.service;
import com.movie.reservation.movie_service.dto.LoginRequest;
import com.movie.reservation.movie_service.dto.UserDTO;
import com.movie.reservation.movie_service.dto.UserRegistrationRequest;
import com.movie.reservation.movie_service.model.Role;
import com.movie.reservation.movie_service.dto.AuthResult;

public interface UserService {
    UserDTO registerUser(UserRegistrationRequest user);
    AuthResult authenticateUser(LoginRequest loginRequest);
    UserDTO getUserById(Long id);
    void updateUserRole(Long id, Role role);
}
