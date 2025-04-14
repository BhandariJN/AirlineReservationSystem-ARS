package com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.response;

import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.enums.FlightStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class FlightResponse {
    private BigDecimal first_class_current_fare;
    private BigDecimal business_class_current_fare;
    private BigDecimal economy_class_current_fare;

    private String flight_number;

    private String flightStatus;
    private LocalDateTime departureTime;
    private Integer journeyHrs;
    private Integer journeyMins;
    private LocalDate flightDate;
    private Long routeId;
    private String routeDesc;

    private String origin;

    private String destination;
    private Long airbusId;
    private String airBusName;

}
