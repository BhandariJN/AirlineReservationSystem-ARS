package com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatisticsResponse {
    private StatisticsSummary summary;
    private ChartData revenueOverTime;
    private ChartData flightStatusDistribution;
    private ChartData topRoutes;
    private ChartData seatOccupancy;
    private List<TopFlight> topFlights;

}
