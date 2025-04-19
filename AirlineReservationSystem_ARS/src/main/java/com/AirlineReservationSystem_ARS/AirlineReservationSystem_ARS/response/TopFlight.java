package com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TopFlight {
    private String flightNumber;
    private String route;
    private double revenue;
    private double occupancyRate;
    private double averageFare;
}