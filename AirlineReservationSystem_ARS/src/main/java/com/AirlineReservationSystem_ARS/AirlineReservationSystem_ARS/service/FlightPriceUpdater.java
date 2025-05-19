package com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.service;

import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.model.Flight;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.repository.FlightRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FlightPriceUpdater {
    private final FlightRepo flightRepo;
    private final DynamicPricingService dynamicPricingService;
    private final FlightFareHistoryService flightFareHistoryService;
    private final FlightService flightService;


    @Scheduled(cron = "${scheduler.flight-price-update-cron}")
    @Transactional
    public void updatePricesForAllFlights() {
        List<Flight> flights = flightService.getAllFlights();
        flights.forEach(this::updateFlightPrice);

        for (Flight flight : flights) {
            flightFareHistoryService.updatePreviousFlightFare(flight);
            updateFlightPrice(flight);
        }

        if (log.isInfoEnabled()) {
            log.info("Updated prices for {} flights", flights.size());
        }

    }


    private void updateFlightPrice(Flight flight) {
        BigDecimal newPrice = dynamicPricingService.calculateFlightPrice(flight);
        flight.setCurrent_fare(newPrice);
    }
}