package com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.service;

import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.model.Flight;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class DynamicPricingService {

    // Pricing parameters (adjust these based on your business rules)
    private static final BigDecimal BASE_MULTIPLIER = BigDecimal.ONE;
    private static final BigDecimal MIN_PRICE_MULTIPLIER = new BigDecimal("0.7");  // 70% of base price
    private static final BigDecimal MAX_PRICE_MULTIPLIER = new BigDecimal("3.0");   // 300% of base price

    private static final BigDecimal DAYS_WEIGHT = new BigDecimal("0.02");
    private static final BigDecimal OCCUPANCY_WEIGHT = new BigDecimal("0.8");
    private static final BigDecimal HOLIDAY_WEIGHT = new BigDecimal("0.15");
    private static final BigDecimal SEASON_WEIGHT = new BigDecimal("0.2");
    private static final BigDecimal EVENT_WEIGHT = new BigDecimal("0.1");

    public BigDecimal calculateFlightPrice(Flight flight) {
        // 1. Get base price from flight schedule
        BigDecimal basePrice = BigDecimal.valueOf(flight.getFlightSchedule().getBaseFare());

        // 2. Calculate price multiplier based on dynamic factors
        BigDecimal priceMultiplier = calculatePriceMultiplier(flight);

        // 3. Apply multiplier with min/max bounds
        BigDecimal calculatedPrice = basePrice.multiply(priceMultiplier)
                .setScale(2, RoundingMode.HALF_UP);

        // 4. Ensure price stays within allowed range
        BigDecimal minPrice = basePrice.multiply(MIN_PRICE_MULTIPLIER);
        BigDecimal maxPrice = basePrice.multiply(MAX_PRICE_MULTIPLIER);

        return calculatedPrice.max(minPrice).min(maxPrice);
    }

    private BigDecimal calculatePriceMultiplier(Flight flight) {
        // Extract all pricing factors
        long daysToFlight = getDaysToFlight(flight);
        BigDecimal occupancyRate = getOccupancyRate(flight);
        boolean isHoliday = flight.getFlightSchedule().isHolidayFlag();
        boolean isPeakSeason = flight.getFlightSchedule().isSeasonalFlag();
        boolean isSpecialEvent = flight.getFlightSchedule().isSpecialFlag();

        // Calculate multiplier components
        BigDecimal daysFactor = DAYS_WEIGHT.multiply(BigDecimal.valueOf(daysToFlight));
        BigDecimal occupancyFactor = OCCUPANCY_WEIGHT.multiply(occupancyRate);
        BigDecimal holidayFactor = isHoliday ? HOLIDAY_WEIGHT : BigDecimal.ZERO;
        BigDecimal seasonFactor = isPeakSeason ? SEASON_WEIGHT : BigDecimal.ZERO;
        BigDecimal eventFactor = isSpecialEvent ? EVENT_WEIGHT : BigDecimal.ZERO;

        // Sum all factors (BASE_MULTIPLIER is typically 1.0)
        return BASE_MULTIPLIER
                .add(daysFactor)
                .add(occupancyFactor)
                .add(holidayFactor)
                .add(seasonFactor)
                .add(eventFactor);
    }

    private long getDaysToFlight(Flight flight) {
        LocalDate departureDate = flight.getFlightSchedule().getDepartureTime().toLocalDate();
        return ChronoUnit.DAYS.between(LocalDate.now(), departureDate);
    }

    private BigDecimal getOccupancyRate(Flight flight) {
        Long seatsBooked = flight.getSeatsBooked();
        Long totalSeats = flight.getFlightSchedule().getAirbus().getCapacity();
        return BigDecimal.valueOf(seatsBooked)
                .divide(BigDecimal.valueOf(totalSeats), 4, RoundingMode.HALF_UP);
    }
}