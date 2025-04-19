package com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.service;

import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.enums.ReservationStatus;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.exception.ResourceNotFoundException;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.model.Flight;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.model.Reservation;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.repository.FlightRepo;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.repository.ReservationRepo;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.request.ReservationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ReservationService {

    private final FlightService flightService;
    private final FlightRepo flightRepo;
    private final ReservationRepo reservationRepo;

    public Reservation reserveFlight(ReservationRequest reservationRequest, Long flightId) {
        Flight flight = flightRepo.findById(flightId).orElseThrow(
                () -> new ResourceNotFoundException("Flight not found")
        );
        Reservation reservation = new Reservation();
        reservation.setFlight(flight);
        reservation.setReservationDate(LocalDate.now());
        reservation.setStatus(ReservationStatus.PENDING);
        reservation.setNoOfPassengers(reservationRequest.getNoOfPassengers());
        reservation.setTotalFare(reservationRequest.getTotalFare());
        reservation.setPnr(generatePNR());

        return reservationRepo.save(reservation);
    }

    public String generatePNR() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().substring(0, 12);
    }


}
