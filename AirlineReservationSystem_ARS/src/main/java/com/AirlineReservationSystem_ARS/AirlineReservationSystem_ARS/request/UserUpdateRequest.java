package com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserUpdateRequest {
    @NotNull
    private String name;
    @NotNull
    private String phone;
    @NotNull
    private String address;
    @NotNull
    private String idNo;
}
