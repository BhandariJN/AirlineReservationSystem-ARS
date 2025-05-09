package com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.model;

import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.enums.ReservationStatus;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.enums.SeatClass;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.response.ReservationResponse;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String pnr;
    private LocalDate reservationDate;
    private Long noOfPassengers;
    private BigDecimal totalFare;


    @Enumerated(EnumType.STRING)
    private ReservationStatus status = ReservationStatus.PENDING;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "fligt_id")
    private Flight flight;




    public static ReservationResponse toReservationResponse(Reservation reservation) {




        ReservationResponse reservationResponse = new ReservationResponse();



        reservationResponse.setFlightNumber(reservation.getFlight().getFlightNumber());
        reservationResponse.setReservationDate(reservation.getReservationDate());
        reservationResponse.setNoOfPassengers(reservation.getNoOfPassengers());
        reservationResponse.setTotalFare(reservation.getTotalFare());
        reservationResponse.setPnr(reservation.getPnr());
        reservationResponse.setStatus(reservation.getStatus());
        return reservationResponse;
    }

}
