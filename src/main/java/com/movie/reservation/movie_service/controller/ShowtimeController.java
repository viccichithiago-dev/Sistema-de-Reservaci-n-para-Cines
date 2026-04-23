package com.movie.reservation.movie_service.controller;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.movie.reservation.movie_service.config.HateoasLinkBuilder;
import com.movie.reservation.movie_service.config.RoleHelper;
import com.movie.reservation.movie_service.dto.AvailableSeatResponse;
import com.movie.reservation.movie_service.dto.ShowtimeRequest;
import com.movie.reservation.movie_service.dto.ShowtimeResponse;
import com.movie.reservation.movie_service.model.Genre;
import com.movie.reservation.movie_service.service.ShowtimeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
@RestController
@RequestMapping("/api/showtimes")
@RequiredArgsConstructor
@Tag(name = "Showtime Management", description = "Operaciones para la gestión de funciones de películas")
public class ShowtimeController {
    private final ShowtimeService showtimeService;
    private final RoleHelper roleHelper;
    private final HateoasLinkBuilder<ShowtimeResponse> hateoasLinkBuilder;

    @GetMapping
    @Operation(summary = "Obtener todos los showtimes", description = "Obtiene una lista paginada de todos los showtimes, con opciones de filtrado por fecha y género.")
    public ResponseEntity<Page<EntityModel<ShowtimeResponse>>> getAllShowtimes(
        @RequestParam(required = false) LocalDate date,
        @RequestParam(required = false) Genre genre,
        @PageableDefault(size = 10) Pageable pageable,
        @AuthenticationPrincipal UserDetails userDetails
    ){
        Page<ShowtimeResponse> showtimes = showtimeService.getAvailableShowtimes(Optional.ofNullable(date), Optional.ofNullable(genre), pageable);
        
        boolean isAdmin = roleHelper.isAdmin(userDetails);
        Page<EntityModel<ShowtimeResponse>> response = showtimes.map(showtime -> 
            hateoasLinkBuilder.buildLinks(showtime, "/api/showtimes", showtime.id(), isAdmin)
        );
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Obtener un showtime por ID", description = "Obtiene los detalles de un showtime específico utilizando su ID.")
    public ResponseEntity<EntityModel<ShowtimeResponse>> getShowtimeById(
        @PathVariable Long id,
        @AuthenticationPrincipal UserDetails userDetails
    ){
        ShowtimeResponse response = showtimeService.getShowtimeById(id);
        boolean isAdmin = roleHelper.isAdmin(userDetails);
        EntityModel<ShowtimeResponse> model = hateoasLinkBuilder.buildLinks(
            response, "/api/showtimes", id, isAdmin);
        return ResponseEntity.ok(model);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crear un nuevo showtime", description = "Crea un nuevo showtime con la información")
    public ResponseEntity<EntityModel<ShowtimeResponse>> createShowtime(@Valid @RequestBody ShowtimeRequest request){
        ShowtimeResponse response = showtimeService.createShowtime(request);
        // Para la creación, asumimos que el admin está creando, por lo que isAdmin es true
        EntityModel<ShowtimeResponse> model = hateoasLinkBuilder.buildLinks(
            response, "/api/showtimes", response.id(), true);
        return ResponseEntity.status(HttpStatus.CREATED).body(model);
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Actualizar un showtime existente", description = "Actualiza los detalles de un showtime existente utilizando su ID. Solo accesible para usuarios con rol ADMIN.")
    public ResponseEntity<EntityModel<ShowtimeResponse>> updateShowtime(
        @PathVariable Long id, 
        @Valid @RequestBody ShowtimeRequest request,
        @AuthenticationPrincipal UserDetails userDetails
    ){
        ShowtimeResponse response = showtimeService.updateShowtime(id, request);
        boolean isAdmin = roleHelper.isAdmin(userDetails);
        EntityModel<ShowtimeResponse> model = hateoasLinkBuilder.buildLinks(
            response, "/api/showtimes", id, isAdmin);
        return ResponseEntity.ok(model);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar un showtime", description = "Elimina un showtime específico utilizando su ID")
    public ResponseEntity<Void> deleteShowtime(@PathVariable Long id){
        showtimeService.deleteShowtime(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{showtimeId}/available-seats")
    @Operation(summary = "Obtener asientos disponibles para un showtime", description = "Obtiene una lista de asientos disponibles para un showtime específico utilizando su ID.")
    public ResponseEntity<EntityModel<List<AvailableSeatResponse>>> getAvailableSeats(
        @PathVariable Long showtimeId,
        @AuthenticationPrincipal UserDetails userDetails
    ){
        List<AvailableSeatResponse> seats = showtimeService.getAvailableSeats(showtimeId);
        
        EntityModel<List<AvailableSeatResponse>> model = EntityModel.of(seats);
        
        // Enlace a sí mismo
        model.add(linkTo(methodOn(ShowtimeController.class).getAvailableSeats(showtimeId, userDetails)).withSelfRel());
        // Enlace al showtime padre
        model.add(linkTo(methodOn(ShowtimeController.class).getShowtimeById(showtimeId, userDetails)).withRel("showtime"));
        
        return ResponseEntity.ok(model);
    }
}
