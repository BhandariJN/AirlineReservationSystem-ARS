package com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.repository;

import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.model.Airbus;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.model.RouteInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RouteInfoRepo extends JpaRepository<RouteInfo, Long> {

    Optional<RouteInfo> findByRouteCodeAndManagedByUserId(String code, Long userId);

    List<RouteInfo> findAllByManagedBy_UserId(Long userId);
    @Query("select r.routeCode from RouteInfo r where r.managedBy.userId=:userId" )
    List<String> findRouteCodesByUserId(@Param("userId") Long userId);

}
