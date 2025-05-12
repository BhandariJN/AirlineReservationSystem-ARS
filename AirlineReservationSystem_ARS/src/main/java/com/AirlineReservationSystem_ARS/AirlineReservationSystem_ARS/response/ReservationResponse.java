package com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.response;

import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.enums.ReservationStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ReservationResponse {
    private String pnr;
    private LocalDate reservationDate;
    private Long noOfPassengers;
    private BigDecimal totalFare;
    private ReservationStatus status;
    private String flightNumber;
}
