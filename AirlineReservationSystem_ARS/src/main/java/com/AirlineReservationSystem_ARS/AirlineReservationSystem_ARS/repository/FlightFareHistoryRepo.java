package com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.repository;

import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.model.FlightFareHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlightFareHistoryRepo extends JpaRepository<FlightFareHistory, Long> {
}
