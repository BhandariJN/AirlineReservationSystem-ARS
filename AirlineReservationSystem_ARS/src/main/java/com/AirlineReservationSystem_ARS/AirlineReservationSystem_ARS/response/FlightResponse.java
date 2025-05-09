package com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.response;

import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.enums.FlightStatus;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.model.Flight;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Data
public class FlightResponse {
    private BigDecimal currentFare;
    private String flightNumber;
    private FlightStatus flightStatus;
    private LocalDateTime flightDate;
    private String origin;
    private String destination;
    private Long airbusId;
    private String airBusName;
    private int journeyHr;
    private int journeyMins;



}