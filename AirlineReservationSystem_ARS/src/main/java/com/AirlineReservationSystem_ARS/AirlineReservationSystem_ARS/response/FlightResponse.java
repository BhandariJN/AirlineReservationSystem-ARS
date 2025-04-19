package com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.response;

import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.enums.FlightStatus;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.model.Flight;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Data
public class FlightResponse {
    private BigDecimal currentFare; // Changed to camelCase for Java conventions
    private String flightNumber;
    private FlightStatus flightStatus;
    private LocalDate flightDate;
    private String origin;
    private String destination;
    private Long airbusId;
    private String airBusName;


}