package com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.service;

import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.exception.AlreadyExistException;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.exception.ResourceNotFoundException;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.model.Airbus;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.repository.AirbusRepo;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.repository.FlightScheduleRepo;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.request.AirbusRequest;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.response.AirbusResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AirbusService {
    private final AirbusRepo airbusRepo;
    private final FlightScheduleRepo flightScheduleRepo;
    private final FlightScheduleService flightScheduleService;

    public Airbus addAirbus(AirbusRequest airbusRequest) {

        airbusRepo.findByAirBusName(airbusRequest.getAirBusName())
                .ifPresent(existingAirbus -> {
                    throw new AlreadyExistException("Airbus with this name already exists");
                });

        return airbusRepo.save(requestToAirbus(airbusRequest));

    }

    public void deleteAirbus(Long airbusId) {
        long count = flightScheduleRepo.countByAirbus_AirbusId(airbusId);
        if (count > 0) {
            throw new AlreadyExistException("Cannot delete Airbus: It is linked to existing flight schedules.");
        }
        Airbus airbus = findById(airbusId);
        airbusRepo.delete(airbus);
    }

    public List<Airbus> getAllAirbus() {

        return airbusRepo.findAll();
    }

    public Airbus updateAirbus(AirbusRequest airbusRequest, Long airbusId) {

        Airbus airbus = updateExistingAirbus(airbusRequest, airbusId);
        return airbusRepo.save(airbus);
    }

    public Airbus requestToAirbus(AirbusRequest airbusRequest) {
        return Airbus.builder().
                airBusName(airbusRequest.getAirBusName())
                .capacity(airbusRequest.getCapacity())
                .airBusModel(airbusRequest.getAirBusModel())
                .airBusType(airbusRequest.getAirBusType())
                .airBusRegistrationNumber(airbusRequest.getAirBusRegistrationNumber())
                .build();
    }


    public AirbusResponse airbusToResponse(Airbus airbus) {
        return AirbusResponse.builder()
                .airBusId(airbus.getAirbusId()).
                airBusName(airbus.getAirBusName())
                .airBusModel(airbus.getAirBusModel())
                .airBusType(airbus.getAirBusType())
                .airBusRegistrationNumber(airbus.getAirBusRegistrationNumber())
                .airBusCapacity(airbus.getCapacity())
                .flightSchedules(airbus.getFlightSchedules().stream().map(flightScheduleService::toResponse).collect(Collectors.toList()).reversed())
                .build();
    }


    public Airbus findById(Long id) {
        return airbusRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("AirBus not found"));
    }

    public Airbus updateExistingAirbus(AirbusRequest airbusRequest, Long airbusId) {
        Airbus airbus = findById(airbusId);
        airbus.setAirBusName(airbusRequest.getAirBusName());
        airbus.setAirBusType(airbusRequest.getAirBusType());
        airbus.setAirBusModel(airbusRequest.getAirBusModel());
        airbus.setAirBusRegistrationNumber(airbusRequest.getAirBusRegistrationNumber());
        airbus.setCapacity(airbusRequest.getCapacity());

        return airbus;
    }

    public List<String> getAllAirbusNames() {
        List<Airbus> airbusList = airbusRepo.findAll();
        if (airbusList.isEmpty()) {
            throw new AlreadyExistException("No Airbus Found");
        }
        return airbusList.stream()
                .map(Airbus::getAirBusName)
                .toList();
    }

    public Airbus getAirbusByName(String airbusName) {
        return airbusRepo.findByAirBusName(airbusName)
                .orElseThrow(() -> new ResourceNotFoundException("Airbus not found"));
    }
}
