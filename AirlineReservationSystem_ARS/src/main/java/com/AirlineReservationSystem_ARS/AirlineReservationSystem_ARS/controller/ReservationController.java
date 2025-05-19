package com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.controller;


import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.exception.AlreadyExistException;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.exception.ResourceNotFoundException;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.request.ReservationRequest;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.response.ApiResponse;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
        } catch (ResourceNotFoundException | AlreadyExistException e) {
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

    @GetMapping("ticket/get/{pnr}")
    public ResponseEntity<?> downloadTicket(@PathVariable String pnr) {
        try {
            byte[] ticket = reservationService.downloadTicket(pnr);
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=ticket.pdf")
                    .body(ticket);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ((
                            "Error generating ticket :" + e.getMessage()
                    ).getBytes()
                    )
            );
        }
    }


}
