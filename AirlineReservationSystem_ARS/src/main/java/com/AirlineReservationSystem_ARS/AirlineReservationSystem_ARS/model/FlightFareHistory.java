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
public class FlightFareHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    BigDecimal avgFlightFare;
    BigDecimal flightBookingRatio;
    @ManyToOne
    @JoinColumn(name = "flight_id")
    Flight flight;

    public FlightFareHistory(Flight flight, BigDecimal currentFare, BigDecimal occupancyRate) {
        this.flight = flight;
        this.avgFlightFare = currentFare;
        this.flightBookingRatio = occupancyRate;
    }
}
