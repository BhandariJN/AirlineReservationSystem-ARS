package com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.repository;

import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.model.Airbus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AirbusRepo extends JpaRepository<Airbus, Long> {
    Optional<Airbus> findByAirBusName(String airBusName);
    List<Airbus> findAllByManagedBy_UserId(Long userId);
    @Query("select a.airBusName from Airbus a where a.managedBy.userId=:userId" )
    List<String> findAirbusNamesByUserId(@Param("userId") Long userId);
}
