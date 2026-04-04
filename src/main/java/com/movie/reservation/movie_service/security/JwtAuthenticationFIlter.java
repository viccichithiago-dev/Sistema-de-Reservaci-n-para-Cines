package com.movie.reservation.movie_service.security;
import java.io.IOException;
import java.util.Collections;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.movie.reservation.movie_service.service.UserDetailsServiceImpl;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFIlter extends OncePerRequestFilter{
    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    // Metodo para filtrar cada solicitud y validar el token JWT
    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;
        // Verificar que el header exista y comience con "Bearer "
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }
        // Extraemos el toker
        jwt = authHeader.substring(7);
        try{
            // Extraemos username del token
            username = jwtUtil.extractUsername(jwt);
            // Verificamos que no este autenticado ya en el contexto
            if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
                // Cargamos el usuario desde la base de datos
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // Validamos el token
                if(jwtUtil.validateToken(jwt,userDetails)){
                    // Creamos autenticacion y establecemos contexto
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + jwtUtil.extractRole(jwt).name())));
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e){
            // En caso de error, simplemente no autenticamos y dejamos que el filtro maneje la respuesta
            logger.error("Error al autenticar el token JWT: " + e.getMessage());
        }
        filterChain.doFilter(request, response);
    }
}
