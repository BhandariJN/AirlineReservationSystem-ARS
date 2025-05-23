package com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.repository;

import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface FlightRepo extends JpaRepository<Flight, Long> {

    Optional<Flight> findByFlightNumber(String flightNumber);

    List<Flight> findByFlightSchedule_DepartureTimeBetween(LocalDateTime start, LocalDateTime end);

    List<Flight> findAllByFlightSchedule_DepartureTimeAfter(LocalDateTime start);

    List<Flight> findAllByFlightSchedule_DepartureTimeAfterAndManagedByUserId(LocalDateTime localDateTime, Long userId);


    @Query("""
                SELECT f FROM Flight f 
                WHERE f.flightSchedule.routeInfo.origin = :origin 
                  AND f.flightSchedule.routeInfo.destination = :destination 
                  AND FUNCTION('DATE', f.flightSchedule.departureTime) = :departureDate
            """)
    List<Flight> findFlightsByOriginDestinationAndDepartureDate(
            @Param("origin") String origin,
            @Param("destination") String destination,
            @Param("departureDate") LocalDate departureDate
    );


}
