package com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.service;

import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.exception.AlreadyExistException;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.exception.ResourceNotFoundException;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.model.Reservation;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.model.User;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.repository.UserRepo;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.request.UserRequest;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.request.UserUpdateRequest;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.response.UserResponse;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.security.config.MinioConfig;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.security.user.AirlineUserDetails;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepo userRepo;
    private final MinioConfig minioConfig;
    private final PasswordEncoder passwordEncoder;

    public UserResponse addUser(UserRequest userRequest) {

        return Optional.of(userRequest).filter(userReq -> !userRepo.existsByEmail(userRequest.getEmail())).map(userReq -> {
            User user = new User();
            user.setEmail(userReq.getEmail());
            user.setPassword(passwordEncoder.encode(userReq.getPassword()));
            user.setName(userReq.getName());
            user.setAddress(userReq.getAddress());
            user.setPhone(userReq.getPhone());
            user.setIdNo(userReq.getIdNumber());
            user.setRoles(Collections.singleton(userReq.getRole()));
            return userToResponse(userRepo.save(user));
        }).orElseThrow(() -> new ResourceNotFoundException(userRequest.getEmail() + "  email already exist!"));


    }


    public ResponseEntity<?> getUserById(Long id) {


        User user = userRepo.findById(id).orElseThrow(() -> new AlreadyExistException("User with id " + id + " does not exist"));

        return ResponseEntity.ok(user);
    }

    public User requestToUser(UserRequest userRequest) {

        return User.builder()
                .name(userRequest.getName())
                .email(userRequest.getEmail())
                .phone(userRequest.getPhone())
                .address(userRequest.getAddress())
                .password(userRequest.getPassword())
                .idNo(userRequest.getIdNumber())
                .build();

    }

    public UserResponse userToResponse(User user) {
        return UserResponse.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .address(user.getAddress())
                .idNo(user.getIdNo())
                .avatarObjectName(user.getAvatarObjectName())
                .build();
    }

    public ResponseEntity<?> getAllUser() {
        List<User> users = userRepo.findAll();

        return ResponseEntity.ok(users.stream().map(this::userToResponse).collect(Collectors.toList()));
    }


    @Transactional
    public ResponseEntity<?> uploadAvatar(MultipartFile file) {
        AirlineUserDetails userDetails = (AirlineUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepo.findByEmail(userDetails.getEmail()).orElseThrow(() -> new ResourceNotFoundException("User with email " + userDetails.getEmail() + " does not exist"));


        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty or not provided");
        }

        try {

            String uuid = UUID.randomUUID().toString();

            minioConfig.UploadFile(file, uuid);
            user.setAvatarObjectName(uuid);
            userToResponse(userRepo.save(user));
            return ResponseEntity.ok("Avatar uploaded successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to upload avatar: " + e);
        }
    }

    public List<UserResponse> getUserOfAirline() {
//        AirlineUserDetails userDetails = (AirlineUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<User> users = userRepo.findAll();
        if (users.isEmpty()) {
            throw new ResourceNotFoundException("Users does not exist");
        }
        return users.stream().map(this::userToResponse).collect(Collectors.toList());
    }


    public UserResponse getSingleLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null || !authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            Long id;
            if (principal instanceof AirlineUserDetails) {
                id = ((AirlineUserDetails) principal).getId();
            } else if (principal instanceof String) {
                String username = (String) principal;
                System.out.println(username);
                return userToResponse(userRepo.findByEmail(username).orElseThrow(() -> new ResourceNotFoundException("User with email " + username + " does not exist")));

            } else {
                throw new ResourceNotFoundException("User with email " + authentication.getPrincipal() + " does not exist");
            }
        }
        AirlineUserDetails userDetails = (AirlineUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userToResponse(userRepo.findById(userDetails.getId()).orElseThrow(() -> new ResourceNotFoundException("User with id " + userDetails.getId() + " does not exist")));
    }

    public String getImgUrl(String objectName) {
        try {
            return minioConfig.getPresignedUrl(objectName);
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public UserResponse updateUser(UserUpdateRequest userUpdateRequest) {
        AirlineUserDetails userDetails = (AirlineUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getEmail();
        User existingUser = userRepo.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User with email " + email + " does not exist"));
        existingUser.setName(userUpdateRequest.getName());
        existingUser.setAddress(userUpdateRequest.getAddress());
        existingUser.setPhone(userUpdateRequest.getPhone());
        existingUser.setIdNo(userUpdateRequest.getIdNo());
        return userToResponse(userRepo.save(existingUser));
    }


}
