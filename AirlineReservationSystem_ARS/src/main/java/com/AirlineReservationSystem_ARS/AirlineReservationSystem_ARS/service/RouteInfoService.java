package com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.service;

import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.exception.AlreadyExistException;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.exception.ResourceNotFoundException;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.model.RouteInfo;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.model.User;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.repository.FlightScheduleRepo;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.repository.RouteInfoRepo;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.repository.UserRepo;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.request.RouteInfoRequest;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.response.RouteInfoResponse;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.security.user.AirlineUserDetails;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class RouteInfoService {

    private final RouteInfoRepo routeInfoRepo;
    private final FlightScheduleRepo flightScheduleRepo;
    private final UserRepo userRepo;


    public RouteInfoResponse addRouteInfo(RouteInfoRequest routeInfo) {
        System.out.println(routeInfo.toString());
        User managedBy = getUser();
        // Check if the route code already exists
        Optional<RouteInfo> existingRouteInfo = routeInfoRepo.findByRouteCodeAndManagedByUserId(routeInfo.getRouteCode(), managedBy.getUserId());
        if (existingRouteInfo.isPresent()) {
            throw new AlreadyExistException("Route code already exists");
        }
        RouteInfo route = requestToRouteInfo(routeInfo);
        route.setManagedBy(managedBy);

        return routeInfoToResponse(routeInfoRepo.save(route));
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

    public List<RouteInfoResponse> getAllRouteInfo() {
        User managedBy = getUser();
        List<RouteInfo> routeInfos = routeInfoRepo.findAllByManagedBy_UserId(managedBy.getUserId());
        if (routeInfos.isEmpty()) {
            throw new ResourceNotFoundException("No Route Info Found");
        }
        return routeInfos.stream().map(
                this::routeInfoToResponse
        ).toList();
    }


    public List<String> getAllRouteCodes() {

        return routeInfoRepo.findRouteCodesByUserId(getUser().getUserId());
    }

    public RouteInfo getRouteInfoByRouteCode(String routeCode) {
        return routeInfoRepo.findByRouteCodeAndManagedByUserId(routeCode,getUser().getUserId()).orElseThrow(() -> new AlreadyExistException("Resource Not Found"));
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

    private User getUser() {
        AirlineUserDetails userDetails = (AirlineUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User managedBy = userRepo.findById(userDetails.getId()).orElseThrow(
                () -> new ResourceNotFoundException("User not found")
        );
        return managedBy;
    }
}
