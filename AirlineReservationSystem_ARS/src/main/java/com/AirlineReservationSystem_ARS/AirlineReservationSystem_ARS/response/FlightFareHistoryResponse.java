package com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class FlightFareHistoryResponse {
    private BigDecimal flightFare;
    private BigDecimal bookingRatio;
    private String flightNumber;
}
