package com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.controller;


import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.exception.ResourceNotFoundException;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.model.Flight;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.repository.FlightRepo;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.request.FlightRequest;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.request.FlightSearchRequest;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.response.ApiResponse;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.response.FlightResponse;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.service.FlightService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/flight")
public class FlightController {

    private final FlightService flightService;


    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllFlights() {
        try {
            List<Flight> flights = flightService.getAllFlights();
            if (flights.isEmpty()) {
                return ResponseEntity.status(404).body(new ApiResponse("No flights found", null));
            }
            List<FlightResponse> responses = flights.stream().map(
                    flightService::toFlightResponse
            ).toList();

            return ResponseEntity.ok(new ApiResponse("Success", responses));
        } catch (Exception e) {
            return ResponseEntity.status(404).body(new ApiResponse(e.getMessage(), null));
        }
    }


    @GetMapping("/all/airline")
    public ResponseEntity<ApiResponse> getAllFlightsOfAirline() {
        try {


            return ResponseEntity.ok(new ApiResponse("Success", flightService.getAllFlightofAirline()));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/search")
    public ResponseEntity<ApiResponse> searchFlights(@RequestBody FlightSearchRequest request) {

        try {
         return    ResponseEntity.ok(new ApiResponse("Success", flightService.searchFlight(request)));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ApiResponse(e.getMessage(), null));
        }
    }

}
