package com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.response;

import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.model.Reservation;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class UserResponse {
    private Long userId;
    private String passengerName;
    private String passengerEmail;
    private String passengerPhone;
    private String passengerAddress;
    private String idNo;
    private String avatarObjectName;
    List<Reservation> reservations;
}
