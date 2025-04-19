package com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.service;

import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.exception.ResourceNotFoundException;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.model.Flight;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.model.FlightSchedule;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.repository.FlightRepo;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.repository.FlightScheduleRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class FeaturePreprationService {

    private final FlightRepo flightRepo;

    public float[] features(long flightId) {
        Flight flight = flightRepo.findById(flightId).orElseThrow(() -> new ResourceNotFoundException("Flight Schedule not found"));
        FlightSchedule flightSchedule = flight.getFlightSchedule();
        LocalDate flightDate = flightSchedule.getDepartureTime().toLocalDate();
        long daystoFlight = ChronoUnit.DAYS.between(LocalDate.now(), flightDate);
        boolean isHoliday = flightSchedule.isHolidayFlag();
        boolean isPeakSeason = flightSchedule.isSeasonalFlag();
        boolean isSpecialEvent = flightSchedule.isSpecialFlag();
        float seatBookedRatio = (float) flight.getSeatsBooked() / flightSchedule.getAirbus().getCapacity();
        return new float[]{daystoFlight, seatBookedRatio, isHoliday ? 1 : 0, isPeakSeason ? 1 : 0, isSpecialEvent ? 1 : 0};
    }


}
