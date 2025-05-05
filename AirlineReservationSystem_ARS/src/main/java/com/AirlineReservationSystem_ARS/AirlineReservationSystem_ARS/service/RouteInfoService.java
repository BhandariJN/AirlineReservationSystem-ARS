package com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.service;

import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.exception.AlreadyExistException;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.exception.ResourceNotFoundException;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.model.RouteInfo;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.repository.FlightScheduleRepo;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.repository.RouteInfoRepo;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.request.RouteInfoRequest;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.response.RouteInfoResponse;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class RouteInfoService {

    private final RouteInfoRepo routeInfoRepo;
    private final FlightScheduleRepo flightScheduleRepo;


    public RouteInfo getRouteInfoById(Long routeId) {
        // Logic to fetch RouteInfo by routeId from the database
        // This is a placeholder implementation
        return routeInfoRepo.findById(routeId)
                .orElseThrow(() -> new AlreadyExistException("Resource Not Found"));
    }


    public RouteInfo addRouteInfo(RouteInfoRequest routeInfo) {
        System.out.println(routeInfo.toString());
        // Check if the route code already exists
        Optional<RouteInfo> existingRouteInfo = routeInfoRepo.findByRouteCode(routeInfo.getRouteCode());
        if (existingRouteInfo.isPresent()) {
            throw new AlreadyExistException("Route code already exists");
        }
        return routeInfoRepo.save(requestToRouteInfo(routeInfo));
    }


    public RouteInfo getRouteInfoByCode(String routeCode) {

        return routeInfoRepo.findByRouteCode(routeCode).orElseThrow(() -> new AlreadyExistException("Resource Not Found"));
    }

    public RouteInfo requestToRouteInfo(RouteInfoRequest request) {
        return RouteInfo.builder()
                .routeDesc(request.getRouteDesc())
                .origin(request.getOrigin())
                .destination(request.getDestination())
                .routeCode(request.getRouteCode())
                .build();
    }

    public RouteInfoResponse routeInfoToResponse(RouteInfo routeInfo) {
        return RouteInfoResponse.builder()

                .id(routeInfo.getRouteId())
                .routeDesc(routeInfo.getRouteDesc())
                .origin(routeInfo.getOrigin())
                .destination(routeInfo.getDestination())
                .routeCode(routeInfo.getRouteCode())
                .build();
    }

    public List<RouteInfo> getAllRouteInfo() {
        List<RouteInfo> routeInfos = routeInfoRepo.findAll();
        if (routeInfos.isEmpty()) {
            throw new AlreadyExistException("No Route Info Found");
        }
        return routeInfos;
    }


    public List<String> getAllRouteCodes() {
        List<RouteInfo> routeInfos = routeInfoRepo.findAll();
        if (routeInfos.isEmpty()) {
            throw new AlreadyExistException("No Route Info Found");
        }
        return routeInfos.stream().map(RouteInfo::getRouteCode).toList();
    }

    public RouteInfo getRouteInfoByRouteCode(String routeCode) {
        return routeInfoRepo.findByRouteCode(routeCode).orElseThrow(() -> new AlreadyExistException("Resource Not Found"));
    }

    @Transactional
    public void deleteRouteInfo(Long routeId) {

        // In your service layer


        RouteInfo routeInfo = routeInfoRepo.findById(routeId)
                .orElseThrow(() -> new ResourceNotFoundException("Route not found"));
        routeInfoRepo.delete(routeInfo); // This should trigger the cascade

    }

    public RouteInfo updateRouteInfo(Long routeId, @Valid RouteInfoRequest routeInfoRequest) {
        RouteInfo routeInfo = routeInfoRepo.findById(routeId)
                .orElseThrow(() -> new ResourceNotFoundException("Route not found"));

        routeInfo.setRouteDesc(routeInfoRequest.getRouteDesc());
        routeInfo.setOrigin(routeInfoRequest.getOrigin());
        routeInfo.setDestination(routeInfoRequest.getDestination());
        routeInfo.setRouteCode(routeInfoRequest.getRouteCode());

        return routeInfoRepo.save(routeInfo);
    }
}
