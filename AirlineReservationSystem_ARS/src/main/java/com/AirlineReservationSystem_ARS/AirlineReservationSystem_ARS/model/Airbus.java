package com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.checkerframework.checker.units.qual.C;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;


@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Airbus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long airbusId;
    private String airBusName;
    private String airBusType;
    private String airBusModel;
    private String airBusRegistrationNumber;
    private Long capacity;


    @OneToMany(mappedBy = "airbus", cascade = {

            CascadeType.PERSIST,
            CascadeType.MERGE,
            CascadeType.REFRESH,
            CascadeType.DETACH,
            CascadeType.REMOVE

    }, orphanRemoval = true)
    @JsonBackReference
    private List<FlightSchedule> flightSchedules;

    @ManyToOne
    @JoinColumn(name = "managedBy_id", nullable = true)
    private User managedBy;




}
