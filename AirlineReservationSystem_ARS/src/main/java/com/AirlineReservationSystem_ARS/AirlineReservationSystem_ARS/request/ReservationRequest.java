package com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ReservationRequest {

    @NotNull
    private String flightNumber;
    @NotNull
    private Long noOfPassengers;
    @NotNull
    private BigDecimal totalFare;
}
