package com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.repository;

import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.model.FlightSchedule;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FlightScheduleRepo  extends JpaRepository<FlightSchedule, Long> {


    @Transactional
    @Modifying
    @Query(value = "delete from flight_schedule where id = :id", nativeQuery = true)
    int deleteFlightScheduleById(Long id);

    long countByAirbus_AirbusId(Long airbusId);

    int countByRouteInfo_RouteId(Long routeInfoRouteId);

}
