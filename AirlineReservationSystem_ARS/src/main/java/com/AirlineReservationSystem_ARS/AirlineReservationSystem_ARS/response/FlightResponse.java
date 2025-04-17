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
    private BigDecimal current_fare;

    private String flight_number;

    private String flightStatus;
    private LocalDate flightDate;
    private String origin;
    private String destination;
    private Long airbusId;
    private String airBusName;

}
