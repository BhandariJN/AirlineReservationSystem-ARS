package com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FlightFeatures {
    private long daysToFlight;
    private float seatBookedRatio;
    private boolean isHoliday;
    private boolean isSeasonal;
    private boolean isSpecialEvent;
}
