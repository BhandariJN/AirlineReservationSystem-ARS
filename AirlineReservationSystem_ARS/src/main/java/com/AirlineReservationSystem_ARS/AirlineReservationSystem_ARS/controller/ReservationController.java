package com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.controller;


import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.exception.AlreadyExistException;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.exception.ResourceNotFoundException;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.request.ReservationRequest;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.response.ApiResponse;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/reservation")
@RestController
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping("/reserve")
    public ResponseEntity<ApiResponse> makeReservation(@Valid @RequestBody ReservationRequest reservationRequest) {

        try {
            return ResponseEntity.ok(new ApiResponse("success", reservationService.reserveFlight(reservationRequest)));
        } catch (ResourceNotFoundException |AlreadyExistException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));

        }
    }

    @GetMapping("user/all")
    public ResponseEntity<ApiResponse> getAllReservationsOfUser() {

        try {
            return ResponseEntity.ok(new ApiResponse("success", reservationService.getAllReservationofUser()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));


        }

    }

    @GetMapping("/pay/{pnr}")
    public ResponseEntity<ApiResponse> payReservation(@PathVariable String pnr) {
        try {
            return ResponseEntity.ok(new ApiResponse(reservationService.makePayment(pnr), null));
        } catch (ResourceNotFoundException | AlreadyExistException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(e.getMessage(), null));

        }
    }


    @GetMapping("/airline/all")
    public ResponseEntity<ApiResponse> getAllReservationsOfAirline() {
        try {

            return ResponseEntity.ok(new ApiResponse("success", reservationService.getAllAirlineReservation()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));

        }
    }


}
