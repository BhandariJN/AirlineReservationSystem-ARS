package com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.response;

import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.model.Reservation;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class UserResponse {
    private Long userId;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String idNo;
    private String avatarObjectName;
    List<ReservationResponse> reservations;
}
