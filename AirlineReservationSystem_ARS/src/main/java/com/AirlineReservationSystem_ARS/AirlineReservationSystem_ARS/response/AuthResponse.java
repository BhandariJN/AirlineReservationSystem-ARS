package com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AuthResponse {
    private Long userId;
    private String message;
    private String token;
}
