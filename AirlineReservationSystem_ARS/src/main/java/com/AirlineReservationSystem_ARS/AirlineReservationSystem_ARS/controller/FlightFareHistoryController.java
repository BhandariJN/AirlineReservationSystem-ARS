package com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.controller;

import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.response.ApiResponse;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.service.FlightFareHistoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/history")
public class FlightFareHistoryController {
    private final FlightFareHistoryService flightFareHistoryService;

    public FlightFareHistoryController(FlightFareHistoryService flightFareHistoryService) {
        this.flightFareHistoryService = flightFareHistoryService;
    }

    @GetMapping("/get")
    public ResponseEntity<ApiResponse> flightFareHistory() {

        return ResponseEntity.ok(new ApiResponse("Success", flightFareHistoryService.getAllFlightFareHistory()));

    }

}
