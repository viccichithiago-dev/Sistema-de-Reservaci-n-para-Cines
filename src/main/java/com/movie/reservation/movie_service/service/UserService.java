package com.movie.reservation.movie_service.service;
import com.movie.reservation.movie_service.dto.LoginRequest;
import com.movie.reservation.movie_service.dto.UserDTO;
import com.movie.reservation.movie_service.dto.UserRegistrationRequest;
import com.movie.reservation.movie_service.model.Role;
import com.movie.reservation.movie_service.dto.AuthResult;
import com.movie.reservation.movie_service.dto.UserResponse;
public interface UserService {
    UserResponse registerUser(UserRegistrationRequest user);
    AuthResult authenticateUser(LoginRequest loginRequest);
    UserResponse getUserById(Long id);
    UserResponse getUserByEmail(String email);
    void updateUserRole(Long id, Role role);
}
