package com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.request;

import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserRequest {
    @NotNull
    private String name;
    @NotNull
    @Email
    private String email;
    @NotNull
    private String phone;
    @NotNull
    private String address;
    @NotNull
    private String password;
    @NotNull
    private String idNumber;

    @NotNull
    private Role role;

}
