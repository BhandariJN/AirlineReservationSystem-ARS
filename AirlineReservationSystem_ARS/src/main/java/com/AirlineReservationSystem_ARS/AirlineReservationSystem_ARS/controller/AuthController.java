package com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.controller;

import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.exception.AlreadyExistException;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.exception.ResourceNotFoundException;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.model.User;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.request.LoginRequest;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.response.ApiResponse;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.response.AuthResponse;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;


    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        try {
            User user = userService.login(loginRequest);



            return ResponseEntity.ok(new ApiResponse("Login successful", user.getUserId()));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ApiResponse(e.getMessage(), null));
        }
    }

}
