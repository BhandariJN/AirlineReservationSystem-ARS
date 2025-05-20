package com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.service;

import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.exception.ResourceNotFoundException;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.model.Flight;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.model.User;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.repository.FlightRepo;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.repository.UserRepo;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.response.FlightResponse;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.security.user.AirlineUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FlightService {
    private final FlightRepo flightRepo;
    private final UserRepo userRepo;
    private final DynamicPricingService pricingService; // You'll need to create this

    public List<Flight> getAllFlights() {
        return flightRepo.findAllByFlightSchedule_DepartureTimeAfter(LocalDateTime.now().plusMinutes(30)).stream()
//                        .filter(
//                        flight -> !flight.getFlightSchedule().getDepartureTime().isBefore(LocalDateTime.now().plusMinutes(30))
//                )
                .sorted(
                        Comparator.comparing(flight -> flight.getFlightSchedule().getDepartureTime())
                )
                .toList();
    }

    public List<FlightResponse> getAllFlightofAirline() {
        List<Flight> flights = flightRepo.findAllByFlightSchedule_DepartureTimeAfterAndManagedByUserId(LocalDateTime.now().plusMinutes(30), getUser().getUserId());
        if (!flights.isEmpty()) {
            return flights.stream().map(
                    this::toFlightResponse
            ).toList();
        }
       throw new ResourceNotFoundException("No flight found");
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

    private User getUser() {
        AirlineUserDetails userDetails = (AirlineUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User managedBy = userRepo.findById(userDetails.getId()).orElseThrow(
                () -> new ResourceNotFoundException("User not found")
        );
        return managedBy;
    }


}