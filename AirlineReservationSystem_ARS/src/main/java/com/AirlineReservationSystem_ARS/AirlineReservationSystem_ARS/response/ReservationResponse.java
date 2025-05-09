package com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.response;

import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.enums.ReservationStatus;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.model.Flight;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.model.User;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
