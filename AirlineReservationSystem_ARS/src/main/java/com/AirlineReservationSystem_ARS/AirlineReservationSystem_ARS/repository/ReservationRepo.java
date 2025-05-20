package com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.repository;

import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.model.Reservation;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.model.User;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepo extends JpaRepository<Reservation, Long> {
    List<Reservation> findAllByUser_UserId(Long userId);

    Reservation findByPnr(String pnr);

    List<Reservation> findAllByFlight_ManagedBy_UserId(Long id);
}
