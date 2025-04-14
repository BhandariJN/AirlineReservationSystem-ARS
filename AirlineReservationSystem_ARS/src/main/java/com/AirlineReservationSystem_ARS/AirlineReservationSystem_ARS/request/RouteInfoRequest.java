package com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RouteInfoRequest {
    @NotNull
    private String routeCode;

    @NotNull
    private String routeDesc;

    @NotNull
    private String origin;

    @NotNull
    private String destination;
}
