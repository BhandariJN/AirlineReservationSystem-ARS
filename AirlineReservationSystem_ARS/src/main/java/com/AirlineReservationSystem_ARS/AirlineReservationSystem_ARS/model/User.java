package com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;
    @NotNull
    private String passengerName;
    @NotNull
    @Column(unique = true)
    private String passengerEmail;
    @NotNull
    private String passengerPhone;
    @NotNull
    private String passengerAddress;
    @NotNull
    private String passengerPassword;
    @NotNull
    private String idNo;

    private String avatarObjectName;


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reservation> reservation;


}
