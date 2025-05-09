package com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.service;

import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.model.Flight;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.repository.FlightRepo;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.response.FlightResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FlightService {
    private final FlightRepo flightRepo;
    private final DynamicPricingService pricingService; // You'll need to create this

    public List<Flight> getAllFlights() {
        return flightRepo.findAll().stream()
                .peek(flight -> {
                    // Calculate dynamic price
                    BigDecimal currentPrice = pricingService.calculateFlightPrice(flight);
                    flight.setCurrent_fare(currentPrice);
                })
                .toList();
    }

    public FlightResponse toFlightResponse(Flight flight) {
        return FlightResponse.builder()
                .flightNumber(flight.getFlightNumber())
                .flightDate(flight.getFlightSchedule().getDepartureTime())
                .currentFare(flight.getCurrent_fare())
                .flightStatus(flight.getFlightStatus())
                .origin(flight.getFlightSchedule().getRouteInfo().getOrigin())
                .destination(flight.getFlightSchedule().getRouteInfo().getDestination())
                .airbusId(flight.getFlightSchedule().getAirbus().getAirbusId())
                .airBusName(flight.getFlightSchedule().getAirbus().getAirBusName())
                .journeyHr(flight.getFlightSchedule().getJourneyHrs())
                .journeyMins(flight.getFlightSchedule().getJourneyMins())
                .build();

    }


}