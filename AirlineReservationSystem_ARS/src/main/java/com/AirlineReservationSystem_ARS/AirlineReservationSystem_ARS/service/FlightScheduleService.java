package com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.service;

import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.enums.FlightStatus;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.exception.AlreadyExistException;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.exception.ResourceNotFoundException;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.model.*;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.repository.AirbusRepo;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.repository.FlightRepo;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.repository.FlightScheduleRepo;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.repository.UserRepo;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.request.FlightScheduleRequest;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.response.ApiResponse;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.response.FlightResponse;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.response.FlightScheduleResponse;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.security.user.AirlineUserDetails;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service

public class FlightScheduleService {


    private final RouteInfoService routeService;


    private final FlightRepo flightRepo;

    private final FlightScheduleRepo flightScheduleRepo;
    private final AirbusRepo airbusRepo;
    private final UserRepo userRepo;
    private final FlightPriceUpdater flightPriceUpdater;


    public List<FlightScheduleResponse.FlightScheduleData> getAllFlightSchedules() {

        List<FlightSchedule> flightSchedules = flightScheduleRepo.findAll();
        return flightSchedules.stream()
                .map(this::toResponse)
                .toList();


    }


    @Transactional
    public FlightSchedule addFlightSchedule(FlightScheduleRequest request) {
        AirlineUserDetails userDetails = (AirlineUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepo.findByEmail(userDetails.getEmail()).orElseThrow(
                () -> new ResourceNotFoundException("User not found")
        );
        if (request.getDepartureTime().isBefore(LocalDateTime.now().plusHours(1))) {
            throw new AlreadyExistException("Please Select Departure Time After 1 Days");
        }
        Flight flight = new Flight();
        String uuid = UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
        System.out.println(uuid);
        flight.setFlightNumber(uuid);
        flight.setFlightStatus(FlightStatus.SCHEDULED);
        flight.setSeatsBooked(0L);
        flight.setCurrent_fare(BigDecimal.valueOf(request.getBaseFare()));
        flight.setManagedBy(user);
        Flight savedFlight = flightRepo.save(flight);

        RouteInfo routeInfo = routeService.getRouteInfoByRouteCode(request.getRouteCode());
        FlightSchedule flightSchedule = flightRequestToFlightSchedule(request);
        flightSchedule.setAirbus(airbusRepo.findByAirBusName(request.getAirbusName())
                .orElseThrow(() -> new ResourceNotFoundException("Airbus Not Found")));
        flightSchedule.setRouteInfo(routeInfo);
        flightSchedule.setFlight(savedFlight);

        // Set the reverse side of the relationship
        savedFlight.setFlightSchedule(flightSchedule);

        // Now save the schedule first (if needed), then update flight
        flightScheduleRepo.save(flightSchedule);
        flightRepo.save(savedFlight); // Optional, but makes relationship consistent

        flightPriceUpdater.updatePricesForAllFlights();
        return flightSchedule;
    }


    @Transactional
    @Modifying
    public void deleteFlightSchedule(Long id) {
        // Check if it exists first
        FlightSchedule schedule = flightScheduleRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Flight Schedule Not Found"));

        // Delete related flight manually
        flightRepo.delete(schedule.getFlight());

        // Then delete schedule using custom query
        int deletedCount = flightScheduleRepo.deleteFlightScheduleById(id);
        if (deletedCount == 0 || flightScheduleRepo.existsById(id)) {
            throw new RuntimeException("Failed to delete flight schedule with id: " + id);
        }
    }


    public FlightSchedule flightRequestToFlightSchedule(FlightScheduleRequest request) {
        return FlightSchedule.builder()
                .departureTime(request.getDepartureTime())
                .journeyHrs(request.getJourneyHrs())
                .journeyMins(request.getJourneyMins())
                .baseFare(request.getBaseFare())
                .holidayFlag(request.isHolidayFlag())
                .seasonalFlag(request.isSeasonalFlag())
                .specialFlag(request.isSpecialFlag())
                .build();
    }

    public FlightScheduleResponse.FlightScheduleData toResponse(FlightSchedule flightSchedule) {

        return FlightScheduleResponse.FlightScheduleData.builder()
                .flightScheduleId(flightSchedule.getId())
                .departureTime(flightSchedule.getDepartureTime())
                .journeyHrs(flightSchedule.getJourneyHrs())
                .journeyMins(flightSchedule.getJourneyMins())
                .baseFare(flightSchedule.getBaseFare())
                .holidayFlag(flightSchedule.isHolidayFlag())
                .seasonalFlag(flightSchedule.isSeasonalFlag())
                .specialFlag(flightSchedule.isSpecialFlag())
                .flightNumber(flightSchedule.getFlight().getFlightNumber())
                .routeName(flightSchedule.getRouteInfo().getRouteCode())
                .airBusName(flightSchedule.getAirbus().getAirBusName())
                .build();

    }


}