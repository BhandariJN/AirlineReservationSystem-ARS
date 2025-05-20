package com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data


public class AirbusResponse {
    private Long airBusId;
    private String airBusName;
    private String airBusType;
    private String airBusModel;
    private String airBusRegistrationNumber;
    private Long airBusCapacity;
    private String managedBy;
}
