package com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class FlightSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime departureTime;
    private Integer journeyHrs;
    private Integer journeyMins;
    private Long baseFare;
    private boolean holidayFlag;
    private boolean seasonalFlag;
    private boolean specialFlag;



    @OneToOne
    @JoinColumn(name = "flight_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Flight flight;

    @ManyToOne
    @JoinColumn(name = "routeInfo_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private RouteInfo routeInfo;


    @ManyToOne
    @JoinColumn(name = "airbus_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Airbus airbus;

}