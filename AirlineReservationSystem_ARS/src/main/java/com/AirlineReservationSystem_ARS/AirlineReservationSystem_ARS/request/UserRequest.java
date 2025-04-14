package com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserRequest {
    @NotNull
    private String passengerName;
    @NotNull
    @Email
    private String passengerEmail;
    @NotNull
    private String passengerPhone;
    @NotNull
    private String passengerAddress;
    @NotNull
    private String passengerPassword;
    @NotNull
    private String idNo;

}
