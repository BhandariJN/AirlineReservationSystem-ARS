package com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ReservationRequest {
    private Long noOfPassengers;
    private BigDecimal totalFare;
}
