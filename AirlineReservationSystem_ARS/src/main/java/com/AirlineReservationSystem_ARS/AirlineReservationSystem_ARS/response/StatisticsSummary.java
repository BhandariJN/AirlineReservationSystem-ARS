package com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatisticsSummary {
    private double totalRevenue;
    private double revenueGrowth;
    private int totalFlights;
    private double flightsGrowth;
    private double bookingRate;
    private double bookingRateGrowth;
    private double averageFare;
    private double averageFareGrowth;
}