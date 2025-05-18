package com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.service;

import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.model.Flight;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.model.FlightFareHistory;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.repository.FlightFareHistoryRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Service
public class FlightFareHistoryService {

    private final DynamicPricingService dynamicPricingService;
    private final FlightFareHistoryRepo flightFareHistoryRepo;

    public void updatePreviousFlightFare(Flight flight) {
        FlightFareHistory flightFareHistory = new FlightFareHistory(
                flight,
                flight.getCurrent_fare(),
                dynamicPricingService.getOccupancyRate(flight)
        );
           flightFareHistoryRepo.save(flightFareHistory);
    }
}
