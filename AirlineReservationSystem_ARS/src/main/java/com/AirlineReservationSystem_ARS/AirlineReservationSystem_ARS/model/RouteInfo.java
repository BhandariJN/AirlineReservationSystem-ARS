package com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class RouteInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long routeId;


    private String routeCode;

    private String routeDesc;

    private String origin;

    private String destination;

    @OneToMany(mappedBy = "routeInfo", cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE,
            CascadeType.REFRESH,
            CascadeType.DETACH,
            CascadeType.REMOVE
    }, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<FlightSchedule> flightSchedules;

    @ManyToOne
    @JoinColumn(name = "managedBy_id", nullable = true)
    private User managedBy;
}
