package com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.request;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class FlightScheduleRequest {
    private LocalDateTime departureTime;
    private Integer journeyHrs;
    private Integer journeyMins;
    private Long baseFare;
    private boolean holidayFlag;
    private boolean seasonalFlag;
    private boolean specialFlag;
    private String routeCode;
    private String airbusName;
}
