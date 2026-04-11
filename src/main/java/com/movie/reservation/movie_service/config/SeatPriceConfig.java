package com.movie.reservation.movie_service.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
@Configuration
@ConfigurationProperties(prefix = "seat.price")
@Validated
public class SeatPriceConfig {
    /**
     * Precio base para los asientos estandars
     */
    @NotNull(message = "El precio base para asientos estandars no puede ser nulo")
    @DecimalMin(value = "0.1", inclusive = false, message = "El precio base para asientos estandars debe ser mayor que 0")
    private Double standardPrice;

    /**
     * Precio para peliculas en 3D
     */
    @NotNull(message = "El precio para peliculas en 3D no puede ser nulo")
    @DecimalMin(value = "1.0", inclusive = false, message = "El precio para peliculas en 3D debe ser mayor que 0")
    private Double threedDMultiplier = 1.5;

    /**
     * Precio para peliculas en IMAX
     */
    @NotNull(message = "El precio para peliculas en IMAX no puede ser nulo")
    @DecimalMin(value = "1.0", inclusive = false, message = "El precio para peliculas en IMAX debe ser mayor que 0")
    private Double imaxMultiplier = 2.0;

    /**
     * Precios de fin de semana
     */
    @NotNull(message = "El precio para clientes semanales no puede ser nulo")
    @DecimalMin(value = "0.1", inclusive = false, message = "El precio para clientes semanales debe ser mayor que 0")
    private Double weekendMultiplier = 1.2;

    /**
     * Usualmente vamos a cobrar en USD
     */
    private String currency = "USD";
    
    // Getters y setters
    public Double getStandardPrice() {
        return standardPrice;
    }
    public void setStandardPrice(Double standardPrice) {
        this.standardPrice = standardPrice;
    }

    public Double getThreedDMultiplier() {
        return threedDMultiplier;
    }

    public void setThreedDMultiplier(Double threedDMultiplier) {
        this.threedDMultiplier = threedDMultiplier;
    }

    public Double getImaxMultiplier() {
        return imaxMultiplier;
    }

    public void setImaxMultiplier(Double imaxMultiplier) {
        this.imaxMultiplier = imaxMultiplier;
    }

    public Double getWeekendMultiplier() {
        return weekendMultiplier;
    }

    public void setWeekendMultiplier(Double weekendMultiplier) {
        this.weekendMultiplier = weekendMultiplier;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
    // Enum para los diferentes tipos de asientos
    public enum SeatType {
        STANDARD,
        THREED_D,
        IMAX
    }
    /**
     * Calcular el precio por su tipo de asiento
     * @param seatType El tipo de asiento
     * @return El precio calculado para el tipo de asiento
     */
    public Double getPriceForSeatType(SeatType seatType) {
        return switch (seatType) {
            case STANDARD -> standardPrice;
            case THREED_D -> standardPrice * threedDMultiplier;
            case IMAX -> standardPrice * imaxMultiplier;
        };

    }
    /**
     * Calcular el precio un dia en especifico
     * @param isWeekend true si el dia es Viernes o Sabbado
     * @return El precio se ajusta segun el dia
     */
    public Double getPriceForDay(boolean isWeekend){
        return isWeekend ? standardPrice * weekendMultiplier : standardPrice;
    }
    /**
     * Calcular el precio final para multiples asientos
     * @param seatCount Cantidad de los asientos
     * @return Precio total
     */
    public Double calculateTotalPrice(int seatCount){
        return standardPrice * seatCount;
    }
}
