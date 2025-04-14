package com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.controller;

import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.exception.AlreadyExistException;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.model.Airbus;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.repository.AirbusRepo;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.request.AirbusRequest;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.response.AirbusResponse;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.response.ApiResponse;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.service.AirbusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@RestController
@RequestMapping("/airbus")
public class AirbusController {

    private final AirbusService airbusService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> createAirbus(@Valid @RequestBody AirbusRequest airbusRequest) {
        try {
            Airbus response = airbusService.addAirbus(airbusRequest);
            return ResponseEntity.ok(new ApiResponse("success", response));
        } catch (AlreadyExistException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllAirbus() {

        try {
            List<Airbus> airbuses = airbusService.getAllAirbus();
            if (airbuses.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse("No airbuses found", null));
            }
            List<AirbusResponse> airbusResponses = airbuses.stream()
                    .map(airbusService::airbusToResponse).toList();

            return ResponseEntity.ok(new ApiResponse("success", airbusResponses));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Error retrieving airbuses", null));
        }


    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> updateAirbus(@PathVariable Long id, @Valid @RequestBody AirbusRequest airbusRequest) {
        try {
            Airbus airbus = airbusService.updateAirbus(airbusRequest, id);
            return ResponseEntity.ok(new ApiResponse("success", airbusService.airbusToResponse(airbus)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(e.getMessage(), null));
        }


    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteAirbus(@PathVariable Long id) {
        try {
            System.out.println(id);
            airbusService.deleteAirbus(id);
            return ResponseEntity.ok(new ApiResponse("Airbus deleted successfully", null));
        } catch (AlreadyExistException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

}
