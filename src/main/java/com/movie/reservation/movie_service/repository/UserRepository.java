package com.movie.reservation.movie_service.repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.movie.reservation.movie_service.model.User;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
        // Authenticacion y busquedas criticas
        Optional<User> findByEmail(String email);
        Optional<User> findByUsername(String username);
        boolean existsByEmail(String email); 
        boolean existsByUsername(String username);

        // Soporte para poder paginar y ordenar
        @Override
        Page<User> findAll(Pageable pageable);
        List<User> findAllByOrderByUsernameAsc();
}
