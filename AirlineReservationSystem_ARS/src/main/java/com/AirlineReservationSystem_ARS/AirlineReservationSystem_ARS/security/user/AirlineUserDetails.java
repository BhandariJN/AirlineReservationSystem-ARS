package com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.security.user;


import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.enums.Role;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AirlineUserDetails implements UserDetails {

    private Long id;
    private String email;
    private String password;
    private Set<Role> roles;


    public static AirlineUserDetails buildUserDetails(User user) {

        return new AirlineUserDetails(
                user.getUserId(),
                user.getEmail(),
                user.getPassword(),
                user.getRoles()
        );

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map(
                role ->
                        new SimpleGrantedAuthority(role.name())
        ).collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
