package com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.service;

import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.exception.AlreadyExistException;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.exception.ResourceNotFoundException;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.model.Airbus;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.model.User;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.repository.AirbusRepo;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.repository.FlightScheduleRepo;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.repository.UserRepo;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.request.AirbusRequest;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.response.AirbusResponse;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.security.user.AirlineUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AirbusService {
    private final AirbusRepo airbusRepo;
    private final FlightScheduleRepo flightScheduleRepo;
    private final FlightScheduleService flightScheduleService;
    private final UserRepo userRepo;

    public AirbusResponse addAirbus(AirbusRequest airbusRequest) {
        AirlineUserDetails userDetails = (AirlineUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User managedBy = userRepo.findById(userDetails.getId()).orElseThrow(
                () -> new ResourceNotFoundException("User not found")
        );
        airbusRepo.findByAirBusName(airbusRequest.getAirBusName())
                .ifPresent(existingAirbus -> {
                    throw new AlreadyExistException("Airbus with this name already exists");
                });
        Airbus airbus = requestToAirbus(airbusRequest);
        airbus.setManagedBy(managedBy);
        return airbusToResponse(airbusRepo.save(airbus));

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
        AirlineUserDetails userDetails = (AirlineUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return airbusRepo.findAllByManagedBy_UserId(userDetails.getId());
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
                .managedBy(airbus.getManagedBy().getName())
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

    public List<String> airbusNames() {

        return
                airbusRepo.findAirbusNamesByUserId(
                        ((AirlineUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId()

                );
    }
}
