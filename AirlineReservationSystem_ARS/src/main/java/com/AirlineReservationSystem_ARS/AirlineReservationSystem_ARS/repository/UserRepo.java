package com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.repository;

import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {

    User findByEmail(String email);

    boolean existsByEmail(@NotNull @Email String email);

}
