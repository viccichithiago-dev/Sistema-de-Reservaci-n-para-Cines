package com.movie.reservation.movie_service.service;
import com.movie.reservation.movie_service.dto.LoginRequest;
import com.movie.reservation.movie_service.dto.UserDTO;
import com.movie.reservation.movie_service.dto.UserRegistrationRequest;
import com.movie.reservation.movie_service.model.Role;

public interface UserService {
    UserDTO registerUser(UserRegistrationRequest user);
    String authenticateUser(LoginRequest loginRequest);
    UserDTO getUserById(Long id);
    void updateUserRole(Long id, Role role);
}
