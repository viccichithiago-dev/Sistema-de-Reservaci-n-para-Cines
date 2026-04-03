package com.movie.reservation.movie_service.service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.movie.reservation.movie_service.dto.LoginRequest;
import com.movie.reservation.movie_service.dto.UserDTO;
import com.movie.reservation.movie_service.dto.UserRegistrationRequest;
import com.movie.reservation.movie_service.model.Role;
import com.movie.reservation.movie_service.model.User;
import com.movie.reservation.movie_service.repository.UserRepository;
import com.movie.reservation.movie_service.security.JwtUtil;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    // Metodo privado
    private UserDTO toResponse(User user) {
    return new UserDTO(
        user.getId(),
        user.getEmail(),
        user.getPassword(),
        user.getRole()
    );
    }
    // Metodo para registrar un usuario
    @Override
    @Transactional
    public UserDTO registerUser(UserRegistrationRequest request){
        // Validaciones
        if(userRepository.existsByEmail(request.email())){
            throw new RuntimeException("El Email ya esta registrado:  "+ request.email());
        }
        if(userRepository.existsByUsername(request.name())){
            throw new RuntimeException("El Nombre de usuario ya esta registrado " + request.name());
        }

        // Una vez validado creamos y guardamos el usuario
        User user = new User(
            request.name(),
            request.email(),
            passwordEncoder.encode(request.password())
        );
        User savedUser = userRepository.save(user);
        return toResponse(savedUser);
    }
    // Metodo para validar un usuario
    @Override
    @Transactional
    public String authenticateUser(LoginRequest request){
        User user = userRepository.findByEmail(request.email())
                    .orElseThrow(()-> new RuntimeException("Usuario no encontrado"));
        if(!passwordEncoder.matches(request.password(), user.getPassword())){
            throw new RuntimeException("Credenciales invalidas");
        }
        return jwtUtil.generateToken(user.getUsername(),user.getRole());
    }

    // Metodo para obtener el usuario por el ID
    @Override
    public UserDTO getUserById(Long id){
        User user = userRepository.findById(id)
                    .orElseThrow(()-> new RuntimeException("Usuario no encontrado con el ID: "+ id));
        return toResponse(user);
    }

    // Metodo para actualizar el ROLE de un Usuario
    @Override
    @Transactional
    public void updateUserRole(Long userId, Role role){
        User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + userId));
                    user.setRole(role);
                    userRepository.save(user);    
    }
}
