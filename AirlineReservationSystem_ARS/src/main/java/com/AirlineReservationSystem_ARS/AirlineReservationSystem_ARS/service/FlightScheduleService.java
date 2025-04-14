package com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.service;

import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.exception.AlreadyExistException;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.exception.ResourceNotFoundException;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.model.Airbus;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.model.Flight;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.model.FlightSchedule;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.model.RouteInfo;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.repository.AirbusRepo;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.repository.FlightRepo;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.repository.FlightScheduleRepo;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.request.FlightScheduleRequest;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.response.ApiResponse;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.response.FlightResponse;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.response.FlightScheduleResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

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


    public List<FlightScheduleResponse.FlightScheduleData> getAllFlightSchedules() {

        List<FlightSchedule> flightSchedules = flightScheduleRepo.findAll();
        return flightSchedules.stream()
                .map(this::toResponse)
                .toList();


    }


    @Transactional
    public FlightSchedule addFlightSchedule(FlightScheduleRequest request) {

        Flight flight = new Flight();
        String uuid = UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
        System.out.println(uuid);
        flight.setFlightNumber(uuid);
        Flight savedFlight = flightRepo.save(flight);


        RouteInfo routeInfo = routeService.getRouteInfoByRouteCode(request.getRouteCode());
        FlightSchedule flightSchedule = flightRequestToFlightSchedule(request);
        flightSchedule.setAirbus(airbusRepo.findByAirBusName(request.getAirbusName()).orElseThrow(() -> new ResourceNotFoundException("Airbus Not Found")));
        flightSchedule.setRouteInfo(routeInfo);
        flightSchedule.setFlight(savedFlight);


        return flightScheduleRepo.save(flightSchedule);

    }




    @Transactional
    @Modifying
    public void deleteFlightSchedule(Long id) {
        // 1. Get the entity with relationships
        flightScheduleRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Flight Schedule Not Found"));


        int deletedCount = flightScheduleRepo.deleteFlightScheduleById(id);


        if(deletedCount == 0) {
            throw new ResourceNotFoundException("Failed to delete flight schedule with id: " + id);
        }

        // 4. Verify deletion
        flightScheduleRepo.flush();
        if (flightScheduleRepo.existsById(id)) {
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
