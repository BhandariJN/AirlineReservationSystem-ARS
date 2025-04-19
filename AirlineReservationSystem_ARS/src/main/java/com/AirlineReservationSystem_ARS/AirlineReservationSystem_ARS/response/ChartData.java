package com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChartData {
    private List<String> labels;
    private List<Number> values;
}