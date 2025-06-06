package com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.service;

import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.enums.ReservationStatus;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.model.Flight;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.model.Reservation;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class DynamicPricingService {


    private static final BigDecimal BASE_MULTIPLIER = BigDecimal.ONE;
    private static final BigDecimal MIN_PRICE_MULTIPLIER = new BigDecimal("0.7");  // 70% of base price
    private static final BigDecimal MAX_PRICE_MULTIPLIER = new BigDecimal("3.0");   // 300% of base price

    private static final BigDecimal DAYS_WEIGHT = new BigDecimal("0.02");
    private static final BigDecimal OCCUPANCY_WEIGHT = new BigDecimal("0.8");
    private static final BigDecimal HOLIDAY_WEIGHT = new BigDecimal("0.15");
    private static final BigDecimal SEASON_WEIGHT = new BigDecimal("0.2");
    private static final BigDecimal EVENT_WEIGHT = new BigDecimal("0.1");

    public BigDecimal calculateFlightPrice(Flight flight) {

        BigDecimal basePrice = BigDecimal.valueOf(flight.getFlightSchedule().getBaseFare());


        BigDecimal priceMultiplier = calculatePriceMultiplier(flight);

        System.out.println("priceMultiplier: " + priceMultiplier);

        BigDecimal calculatedPrice = basePrice.multiply(priceMultiplier)
                .setScale(2, RoundingMode.HALF_UP);


        BigDecimal minPrice = basePrice.multiply(MIN_PRICE_MULTIPLIER);
        BigDecimal maxPrice = basePrice.multiply(MAX_PRICE_MULTIPLIER);

        return calculatedPrice.max(minPrice).min(maxPrice);
    }

    private BigDecimal calculatePriceMultiplier(Flight flight) {

        long daysToFlight = getDaysToFlight(flight);
        BigDecimal occupancyRate = getOccupancyRate(flight);
        System.out.println("occupancyRate" + occupancyRate);
        boolean isHoliday = flight.getFlightSchedule().isHolidayFlag();
        boolean isPeakSeason = flight.getFlightSchedule().isSeasonalFlag();
        boolean isSpecialEvent = flight.getFlightSchedule().isSpecialFlag();


        BigDecimal daysFactor = DAYS_WEIGHT.multiply(BigDecimal.valueOf(daysToFlight));
        BigDecimal occupancyFactor = OCCUPANCY_WEIGHT.multiply(occupancyRate);
        BigDecimal holidayFactor = isHoliday ? HOLIDAY_WEIGHT : BigDecimal.ZERO;
        BigDecimal seasonFactor = isPeakSeason ? SEASON_WEIGHT : BigDecimal.ZERO;
        BigDecimal eventFactor = isSpecialEvent ? EVENT_WEIGHT : BigDecimal.ZERO;


        return BASE_MULTIPLIER
                .add(daysFactor)
                .add(occupancyFactor)
                .add(holidayFactor)
                .add(seasonFactor)
                .add(eventFactor);
    }

    public long getDaysToFlight(Flight flight) {
        LocalDate departureDate = flight.getFlightSchedule().getDepartureTime().toLocalDate();
        return ChronoUnit.DAYS.between(LocalDate.now(), departureDate);
    }

    public BigDecimal getOccupancyRate(Flight flight) {
        long seatsBooked = Optional.ofNullable(flight.getReservations())
                .stream()
                .flatMap(List::stream)
                .filter(Objects::nonNull)
                .filter(reservation -> ReservationStatus.CONFIRMED.equals(reservation.getStatus()))
                .mapToLong(Reservation::getNoOfPassengers)
                .sum();
        System.out.println(flight.getFlightNumber() + "seatsBooked: " + seatsBooked);
        Long totalSeats = flight.getFlightSchedule().getAirbus().getCapacity();
        System.out.println(flight.getFlightNumber() + "totalSeats: " + totalSeats);
        return BigDecimal.valueOf(seatsBooked)
                .divide(BigDecimal.valueOf(totalSeats), 4, RoundingMode.HALF_UP);
    }
}