package com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class FlightScheduleResponse {

    private List<String> routeInfo;
    private List<String> airbus;
    private List<FlightScheduleData> flightSchedule;

    @Data
    @Builder
    public static class FlightScheduleData {
        private Long flightScheduleId;
        private LocalDateTime departureTime;
        private Integer journeyHrs;
        private Integer journeyMins;
        private Long baseFare;
        private boolean holidayFlag;
        private boolean seasonalFlag;
        private boolean specialFlag;
        private String flightNumber;
        private String routeName;
        private String airBusName;
    }
}
