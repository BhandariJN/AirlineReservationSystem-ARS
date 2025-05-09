package com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.controller;

import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.exception.ResourceNotFoundException;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.model.User;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.request.UserRequest;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.response.ApiResponse;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.response.UserResponse;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addUser(
            @RequestBody @Valid UserRequest userRequest) {

        try {
            UserResponse user = userService.addUser(userRequest);
            return ResponseEntity.ok(new ApiResponse("User created successfully", user));
        } catch (Exception e) {
            // Log exception (you can use a logger here)
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), null));
        }
    }


    @GetMapping("/all")
    public ResponseEntity<?> getAllUser() {
        return userService.getAllUser();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PostMapping("/avatar/{id}")
    public ResponseEntity<?> uploadAvatar(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        return userService.uploadAvatar(id, file);
    }

    @GetMapping("/get/users")
    public ResponseEntity<ApiResponse> getUserOfAirline(){
        try {
            return ResponseEntity.ok(new ApiResponse("success", userService.getUserOfAirline()));
        }
        catch (ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }


}
