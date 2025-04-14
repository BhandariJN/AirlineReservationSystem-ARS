package com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Cancellation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String cancel_reason;
    private String cancel_date;
    private String cancel_time;
    private String cancel_status;
    private Long refund_amount;
    private Long reservation_id;


}
