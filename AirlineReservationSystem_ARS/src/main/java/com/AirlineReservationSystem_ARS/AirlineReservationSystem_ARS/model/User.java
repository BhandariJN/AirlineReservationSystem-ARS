package com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.model;

import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private String name;

    @NotNull
    @Column(unique = true)
    private String email;

    @NotNull
    private String phone;

    @NotNull
    private String address;

    @NotNull
    private String password;

    private String idNo;

    private String avatarObjectName;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reservation> reservation;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;


    @OneToMany(mappedBy = "managedBy", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Flight> flight;


}
