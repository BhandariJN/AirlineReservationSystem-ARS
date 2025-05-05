package com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.security.user;

import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.model.User;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AirlineUserDetailsService implements UserDetailsService {
    private final UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = Optional.of(
                userRepo.findByEmail(email)).orElseThrow(
                () -> new UsernameNotFoundException("User Not Found")
        );
        return AirlineUserDetails.buildUserDetails(user);
    }
}
