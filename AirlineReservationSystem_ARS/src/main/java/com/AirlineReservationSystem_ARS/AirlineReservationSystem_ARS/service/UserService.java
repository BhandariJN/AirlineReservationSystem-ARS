package com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.service;

import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.config.MinioConfig;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.exception.AlreadyExistException;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.exception.AuthenticationException;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.exception.ResourceNotFoundException;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.model.User;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.repository.UserRepo;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.request.LoginRequest;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.request.UserRequest;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.response.UserResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepo userRepo;
    private final MinioConfig minioConfig;

    public User addUser(UserRequest userRequest) {


        // Check if user already exists
        userExistsByEmail(userRequest.getPassengerEmail());


        // Save user to repository

        return userRepo.save(requestToUser(userRequest));


    }


    public Optional<User> userExistsByEmail(String email) {
        return Optional.ofNullable(userRepo.findByPassengerEmail(email)).orElseThrow(() -> new AuthenticationException("User not found"));
    }

    public ResponseEntity<?> getUserById(Long id) {


        User user = userRepo.findById(id)
                .orElseThrow(() ->
                        new AlreadyExistException("User with id " + id + " does not exist"));

        return ResponseEntity.ok(user);
    }

    public User requestToUser(UserRequest userRequest) {

        return User.builder()
                .passengerName(userRequest.getPassengerName())
                .passengerEmail(userRequest.getPassengerEmail())
                .passengerPhone(userRequest.getPassengerPhone())
                .passengerAddress(userRequest.getPassengerAddress())
                .passengerPassword(userRequest.getPassengerPassword())
                .idNo(userRequest.getIdNo())
                .build();

    }

    public UserResponse userToResponse(User user) {
        return UserResponse.builder()
                .userId(user.getUserId())
                .passengerName(user.getPassengerName())
                .passengerEmail(user.getPassengerEmail())
                .passengerPhone(user.getPassengerPhone())
                .passengerAddress(user.getPassengerAddress())
                .idNo(user.getIdNo())
                .avatarObjectName(user.getAvatarObjectName())
                .reservations(user.getReservation())
                .build();
    }

    public ResponseEntity<?> getAllUser() {
        List<User> users = userRepo.findAll();

        return ResponseEntity.ok(users.stream().map(this::userToResponse).collect(Collectors.toList()));
    }


    @Transactional
    public ResponseEntity<?> uploadAvatar(Long id, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty or not provided");
        }

        try {
            User user = userRepo.findById(id)
                    .orElseThrow(() -> new AlreadyExistException("User with id " + id + " does not exist"));

            String uuid = UUID.randomUUID().toString();

            minioConfig.UploadFile(file, uuid);
            user.setAvatarObjectName(uuid);
            userRepo.save(user);

            return ResponseEntity.ok("Avatar uploaded successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to upload avatar: " + e);
        }
    }

    public User login(LoginRequest loginRequest) {
        // Check if user exists
        Optional<User> user = userExistsByEmail(loginRequest.getUsername());
        if (user.isPresent()) {

            if (!user.get().getPassengerPassword().equals(loginRequest.getPassword())) {
                throw new ResourceNotFoundException("Invalid password");
            }

            return user.get();
        }
        throw new ResourceNotFoundException("User with email " + loginRequest.getUsername() + " does not exist");
    }
}
