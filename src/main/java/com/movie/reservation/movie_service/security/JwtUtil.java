package com.movie.reservation.movie_service.security;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.movie.reservation.movie_service.model.Role;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
@Component
public class JwtUtil {
    private final SecretKey secretKey;
    private final long expiration;
    public JwtUtil(
        @Value("${jwt.secret}") String secret,
        @Value("${jwt.expiration}") long expiration
    ){
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expiration = expiration;
    }
    // Metodo para generar un token JWT
    public String generateToken(String username, Role role){
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + expiration);
        
        // Retornamos el toker JWT con el nombre y su ROL
        return Jwts.builder()
            .subject(username)
            .claim("role",role.name())
            .issuedAt(now)
            .expiration(expirationDate)
            .signWith(secretKey)
            .compact();
    }
    // Metodo para poder validar un Token
    public boolean validateToken(String token){
        try{
            Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token);
            return true;
        } catch(JwtException | IllegalArgumentException e){
            return false;
        }
    }
    // Metodo para poder obtener el usuario atravez de su Token
    public String extractUsername(String token){
        return Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .getSubject();
    }
    // Metodo para poder obtener el ROL de un usuario atravez de su Token
    public Role extractRole(String token){
        String role = Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .get("role", String.class);
        return Role.valueOf(role);
    }
    // Validacion extra del Token
    public boolean validateToken(String token, UserDetails userDetails){
        try{
            final String extractedUsername = extractUsername(token);
            return (extractedUsername != null && extractedUsername.equals(userDetails.getUsername()))
                && validateToken(token); // validamos firma y expriacion
        } catch(Exception e){
            return false;
        }
    }
}
