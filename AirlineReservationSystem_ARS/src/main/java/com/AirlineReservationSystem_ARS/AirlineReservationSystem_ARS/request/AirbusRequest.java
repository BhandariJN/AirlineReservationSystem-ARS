package com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AirbusRequest {

    @NotNull(message = "Airbus name cannot be null")
    private String airBusName;
    @NotNull(message = "Airbus type cannot be null")
    private String airBusType;
    @NotNull(message = "Airbus model cannot be null")
    private String airBusModel;
    @NotNull(message = "Airbus registration number cannot be null")
    private String airBusRegistrationNumber;
    @NotNull(message = "Airbus capacity cannot be null")
    private Long capacity;

}
