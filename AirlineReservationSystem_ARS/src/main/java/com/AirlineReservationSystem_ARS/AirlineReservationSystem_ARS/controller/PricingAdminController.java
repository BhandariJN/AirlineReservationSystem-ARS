package com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.controller;

import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.service.FlightPriceUpdater;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/pricing")
@RequiredArgsConstructor
public class PricingAdminController {
    private final FlightPriceUpdater priceUpdater;

    @PostMapping("/refresh")
    public ResponseEntity<String> triggerPriceUpdate() {
        priceUpdater.updatePricesForAllFlights();
        return ResponseEntity.ok("Price refresh initiated");
    }
}