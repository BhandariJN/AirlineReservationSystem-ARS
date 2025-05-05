package com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.controller;

import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.exception.AlreadyExistException;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.exception.ResourceNotFoundException;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.model.RouteInfo;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.request.RouteInfoRequest;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.response.ApiResponse;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.response.RouteInfoResponse;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.service.RouteInfoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
@RestController
@RequestMapping("/route")
public class RouteInfoController {

    private final RouteInfoService infoService;
    private final RouteInfoService routeInfoService;


    @RequestMapping("/add")
    public ResponseEntity<ApiResponse> addRoute(@RequestBody @Valid RouteInfoRequest routeInfoRequest) {
        try {
            RouteInfo info = infoService.addRouteInfo(routeInfoRequest);
            return ResponseEntity.ok(new ApiResponse("Route added successfully", infoService.routeInfoToResponse(info)));
        } catch (Exception e) {
            return ResponseEntity.status(409).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @RequestMapping("/all")
    public ResponseEntity<ApiResponse> getAllRoute() {
        try {
            List<RouteInfo> routeInfos = infoService.getAllRouteInfo();
            List<RouteInfoResponse> responses = routeInfos.stream().map(
                    routeInfoService::routeInfoToResponse
            ).toList();

            return ResponseEntity.ok(new ApiResponse("success", responses));
        } catch (AlreadyExistException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/delete/{routeId}")
    public ResponseEntity<ApiResponse> deleteRoute(@PathVariable Long routeId) {
        try {
            infoService.deleteRouteInfo(routeId);
            return ResponseEntity.ok(new ApiResponse("Route deleted successfully", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/update/{routeId}")
    public ResponseEntity<ApiResponse> updateRoute(@PathVariable Long routeId, @RequestBody @Valid RouteInfoRequest routeInfoRequest) {
        try {
            RouteInfo updatedRoute = infoService.updateRouteInfo(routeId, routeInfoRequest);
            return ResponseEntity.ok(new ApiResponse("Route updated successfully", infoService.routeInfoToResponse(updatedRoute)));
        } catch (AlreadyExistException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), null));
        }
    }

}
