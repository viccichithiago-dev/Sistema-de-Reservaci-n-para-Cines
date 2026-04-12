package com.movie.reservation.movie_service.exception;
// Clase base para excepciones de negocio personalizadas
public abstract class BusinessException extends RuntimeException {
    private final int statusCode;
    protected BusinessException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
    public int getStatusCode() {
        return statusCode;
    }
}
