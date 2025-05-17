package com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.model;

import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.enums.FlightStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String flightNumber;
    private Long seatsBooked;


    //Dynamic Fare Calculation
    private BigDecimal current_fare;

    @Enumerated(EnumType.STRING)
    private FlightStatus flightStatus;

    @OneToOne(mappedBy = "flight", cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE,
            CascadeType.REFRESH,
            CascadeType.DETACH,
            CascadeType.REMOVE
    },
            fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "flight_id", referencedColumnName = "id", nullable = false)
    private FlightSchedule flightSchedule;

    @OneToMany(mappedBy = "flight", cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE,
            CascadeType.REFRESH,
            CascadeType.DETACH,
            CascadeType.REMOVE
    }, orphanRemoval = true)
    private List<Reservation> reservations;

    @OneToMany(mappedBy = "flight", cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE,
            CascadeType.REFRESH,
            CascadeType.DETACH,
            CascadeType.REMOVE

    },
            fetch = FetchType.EAGER, orphanRemoval = true)

    private List<FlightFareHistory> dailyAvgFlightFareHistory;


    @ManyToOne
    @JoinColumn(name = "managedBy_id", nullable = true)
    private User managedBy;
}
