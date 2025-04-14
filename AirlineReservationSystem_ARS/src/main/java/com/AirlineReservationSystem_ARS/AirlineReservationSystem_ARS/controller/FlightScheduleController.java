package com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.controller;


import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.exception.AlreadyExistException;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.exception.ResourceNotFoundException;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.model.FlightSchedule;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.repository.FlightScheduleRepo;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.request.FlightScheduleRequest;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.response.ApiResponse;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.response.FlightScheduleResponse;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.service.AirbusService;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.service.FlightScheduleService;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.service.RouteInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RequiredArgsConstructor
@RestController
@RequestMapping("/flightschedule")
public class FlightScheduleController {


    private final FlightScheduleService flightScheduleService;
    private final FlightScheduleRepo flightScheduleRepo;
    private final RouteInfoService routeService;
    private final AirbusService airbusService;


//    @GetMapping("/allSchedules")
//    public ResponseEntity<?> AllFlightSchedules() {
//        try {
//            List<FlightScheduleResponse.FlightScheduleData> response = flightScheduleService.getAllFlightSchedules();
//            List<String> airbusNames = airbusService.getAllAirbusNames();
//            List<String> routeCodes = routeService.getAllRouteCodes();
//            FlightScheduleResponse schedule=  FlightScheduleResponse.builder()
//                    .flightSchedule(response)
//                    .airbus(airbusNames)
//                    .routeInfo(routeCodes)
//                    .build();
//            return ResponseEntity.ok(new ApiResponse("Success", schedule));
//        } catch (AlreadyExistException | ResourceNotFoundException e) {
//            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), null));
//        }
//    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllFlightSchedules() {
        try{
            List<FlightScheduleResponse.FlightScheduleData> response = flightScheduleService.getAllFlightSchedules();
            List<String> airbusNames = airbusService.getAllAirbusNames();
            List<String> routeCodes = routeService.getAllRouteCodes();
            FlightScheduleResponse schedule=  FlightScheduleResponse.builder()
                    .flightSchedule(response)
                    .airbus(airbusNames)
                    .routeInfo(routeCodes)
                    .build();
            return ResponseEntity.ok(new ApiResponse("Success", schedule));
        } catch (AlreadyExistException | ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), null));
        }


    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addFlightSchedule(
            @RequestBody FlightScheduleRequest flightScheduleRequest
    ) {

        try {
            FlightSchedule flightSchedule =flightScheduleService.addFlightSchedule(flightScheduleRequest);

            return ResponseEntity.ok(new ApiResponse("Success", flightScheduleService.toResponse(flightSchedule)));
        } catch (AlreadyExistException|ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), null));
        }
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteFlightSchedule(@PathVariable Long id) {
        try {
            flightScheduleService.deleteFlightSchedule(id);
            return ResponseEntity.ok(new ApiResponse("Flight schedule deleted successfully", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)  // More appropriate status
                    .body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()  // Catch other potential issues
                    .body(new ApiResponse("Error deleting flight schedule: " + e.getMessage(), null));
        }
    }
}
