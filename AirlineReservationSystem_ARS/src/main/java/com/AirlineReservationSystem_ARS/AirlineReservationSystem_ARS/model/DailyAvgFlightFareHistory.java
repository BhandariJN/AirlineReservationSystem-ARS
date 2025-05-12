package com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DailyAvgFlightFareHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    BigDecimal avgFlightFare;
    float flightBookingRatio;
    @ManyToOne
    @JoinColumn(name = "flight_id")
    Flight flight;

}
