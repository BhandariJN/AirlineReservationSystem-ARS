package com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.repository;

import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface FlightRepo extends JpaRepository<Flight, Long> {

    Optional<Flight> findByFlightNumber(String flightNumber);

    List<Flight> findByFlightSchedule_DepartureTimeBetween(LocalDateTime start, LocalDateTime end);
}
