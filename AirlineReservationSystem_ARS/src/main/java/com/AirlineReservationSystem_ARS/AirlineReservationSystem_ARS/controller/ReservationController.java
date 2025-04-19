package com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.controller;


import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.request.ReservationRequest;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.response.ApiResponse;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/reservation")
@RestController
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping("/make/{flightId}")
    public ResponseEntity<ApiResponse> makeReservation(@Valid ReservationRequest reservationRequest, @PathVariable Long flightId) {
       return ResponseEntity.ok(new ApiResponse("success", reservationService.reserveFlight(reservationRequest, flightId)));
    }
}
