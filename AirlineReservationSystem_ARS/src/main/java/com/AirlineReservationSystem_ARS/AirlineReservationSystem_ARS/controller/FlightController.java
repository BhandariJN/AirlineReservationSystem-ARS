package com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.controller;


import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.request.FlightRequest;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.response.ApiResponse;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.service.FlightService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/flights")
public class FlightController {

    private final FlightService flightService;


    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllFlights() {

        return ResponseEntity.ok(new ApiResponse("Success", flightService.getAllFlights()));


    }

    @PutMapping("/add")
    public ResponseEntity<ApiResponse> updateFlight(
            @RequestBody FlightRequest flightRequest
    ) {

        return ResponseEntity.ok(new ApiResponse("Success", flightService.addFlight(flightRequest)));
    }
}
