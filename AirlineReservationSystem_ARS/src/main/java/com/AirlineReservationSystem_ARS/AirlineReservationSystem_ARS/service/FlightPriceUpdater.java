package com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.service;

import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.model.Flight;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.repository.FlightRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FlightPriceUpdater {
    private final FlightRepo flightRepo;
    private final DynamicPricingService pricingService;

    @Transactional
    public void updatePricesForAllFlights() {
        List<Flight> flights = flightRepo.findAll();
        flights.forEach(this::updateFlightPrice);
        if (log.isInfoEnabled()) {
            log.info("Updated prices for {} flights", flights.size());
        }
    }

    @Transactional
    public void updatePricesForFlightsDepartingSoon() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime threshold = now.plusDays(3); // Update flights departing in next 3 days
        List<Flight> flights = flightRepo.findByFlightSchedule_DepartureTimeBetween(now, threshold);

        flights.forEach(this::updateFlightPrice);
        log.info("Updated prices for {} imminent flights", flights.size());
    }

    private void updateFlightPrice(Flight flight) {
        BigDecimal newPrice = pricingService.calculateFlightPrice(flight);
        flight.setCurrent_fare(newPrice);
    }
}