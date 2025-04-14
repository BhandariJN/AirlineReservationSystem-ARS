package com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.response;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RouteInfoResponse {

    private Long id;
    private String routeCode;


    private String routeDesc;


    private String origin;


    private String destination;
}
