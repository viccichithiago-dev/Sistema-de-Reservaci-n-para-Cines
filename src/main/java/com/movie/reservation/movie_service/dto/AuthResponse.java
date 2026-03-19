package com.movie.reservation.movie_service.dto;

import java.time.LocalDateTime;

public record AuthResponse(
    String token,
    LocalDateTime expiresIn,
    String userInfo
) {}
