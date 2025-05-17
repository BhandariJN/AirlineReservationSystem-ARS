package com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.service;

import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.enums.ReservationStatus;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.exception.AlreadyExistException;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.exception.ResourceNotFoundException;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.model.Flight;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.model.Reservation;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.model.User;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.repository.FlightRepo;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.repository.ReservationRepo;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.repository.UserRepo;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.request.ReservationRequest;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.response.ReservationResponse;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.security.user.AirlineUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ReservationService {

    private final FlightService flightService;
    private final FlightRepo flightRepo;
    private final ReservationRepo reservationRepo;
    private final UserRepo userRepo;

    public ReservationResponse reserveFlight(ReservationRequest reservationRequest) {


        Flight flight = flightExists(reservationRequest);
        seatAvalibilityCheck(reservationRequest);

        Reservation reservation = new Reservation();
        // get user form security context holder
        AirlineUserDetails userDetails = (AirlineUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepo.findById(userDetails.getId()).orElseThrow(
                () -> new ResourceNotFoundException("User not found")
        );
        reservation.setUser(user);
        reservation.setFlight(flight);
        reservation.setReservationDate(LocalDate.now());
        reservation.setStatus(ReservationStatus.PENDING);
        reservation.setNoOfPassengers(reservationRequest.getNoOfPassengers());
        reservation.setTotalFare(reservationRequest.getTotalFare());
        reservation.setPnr(generatePNR());

        return Reservation.toReservationResponse(reservationRepo.save(reservation));
    }


    public String generatePNR() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().substring(0, 12);
    }


    public List<ReservationResponse> getAllReservationofUser() {
        AirlineUserDetails airlineUserDetails = (AirlineUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();


        List<Reservation> reservations = Optional.ofNullable(reservationRepo.findAllByUser_UserId(airlineUserDetails.getId()))
                .orElseThrow(
                        () -> new ResourceNotFoundException("Reservation not found")
                );


        return reservations.stream().map(Reservation::toReservationResponse).sorted(Comparator.comparing(
                ReservationResponse::getReservationDate).reversed()
        ).toList();

    }

    public String makePayment(String pnr) {
        Reservation reservation = Optional.of(reservationRepo.findByPnr(pnr)).orElseThrow(
                () -> new ResourceNotFoundException("Reservation not found")
        );
        switch (reservation.getStatus()) {
            case PENDING: {
                reservation.setStatus(ReservationStatus.CONFIRMED);
                reservationRepo.save(reservation);
                return "Payment of " + reservation.getPnr() + " has been paid successfully";

            }
            case CONFIRMED: {
                throw new AlreadyExistException("Reservation is already confirmed");
            }
            case CANCELLED: {
                throw new AlreadyExistException("Reservation is already cancelled");
            }
            default:
                return null;
        }


    }

    public List<ReservationResponse> getAllAirlineReservation() {
        AirlineUserDetails airlineUserDetails = (AirlineUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userEmail = airlineUserDetails.getEmail();


        return null;

    }


    public Flight flightExists(ReservationRequest reservationRequest) {
        return flightRepo.findByFlightNumber(reservationRequest.getFlightNumber()).orElseThrow(
                () -> new ResourceNotFoundException("Flight not found")
        );
    }

    public void seatAvalibilityCheck(ReservationRequest reservationRequest) {
        Flight flight = flightExists(reservationRequest);
        Long capacity = flight.getFlightSchedule().getAirbus().getCapacity();
        Long reservedSeats = flight.getReservations().stream().mapToLong(
                Reservation::getNoOfPassengers
        ).sum();
        Long requestedSeats = reservationRequest.getNoOfPassengers();
        if (reservedSeats + requestedSeats > capacity) {
            throw new AlreadyExistException("Sorry!!  Requested " + reservationRequest.getNoOfPassengers() + " seats are not available right now!");
        }

    }


}
