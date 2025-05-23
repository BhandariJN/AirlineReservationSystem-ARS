package com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.request;

import lombok.Data;

@Data
public class FlightSearchRequest {
    private String origin;
    private String destination;
    private String departureTime;
}
