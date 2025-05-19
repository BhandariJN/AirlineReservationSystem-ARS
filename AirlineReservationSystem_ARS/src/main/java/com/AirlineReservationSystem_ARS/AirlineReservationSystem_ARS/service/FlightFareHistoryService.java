package com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.service;

import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.model.Flight;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.model.FlightFareHistory;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.repository.FlightFareHistoryRepo;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.response.FlightFareHistoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class FlightFareHistoryService {

    private final DynamicPricingService dynamicPricingService;
    private final FlightFareHistoryRepo flightFareHistoryRepo;

    public void updatePreviousFlightFare(Flight flight) {
        FlightFareHistory flightFareHistory = new FlightFareHistory(
                flight,
                flight.getCurrent_fare(),
                dynamicPricingService.getOccupancyRate(flight)
        );
        flightFareHistoryRepo.save(flightFareHistory);
    }


    public Map<String, List<FlightFareHistoryResponse>> getAllFlightFareHistory() {
        List<FlightFareHistory> flightFareHistories = flightFareHistoryRepo.findAll();
        List<FlightFareHistoryResponse> flightFareHistoryResponses = flightFareHistories.stream().map(
                this::toFlightFareHistoryResponse
        ).toList();
        Map<String, List<FlightFareHistoryResponse>> groupedReport = flightFareHistoryResponses.stream().collect(
                Collectors.groupingBy(FlightFareHistoryResponse::getFlightNumber)
        );
        return groupedReport;


    }

    public FlightFareHistoryResponse toFlightFareHistoryResponse(FlightFareHistory flightFareHistory) {
        FlightFareHistoryResponse flightFareHistoryResponse = new FlightFareHistoryResponse();
        flightFareHistoryResponse.setFlightFare(flightFareHistory.getFlightFare());
        flightFareHistoryResponse.setFlightNumber(flightFareHistory.getFlight().getFlightNumber());
        flightFareHistoryResponse.setBookingRatio(flightFareHistory.getFlightBookingRatio());

        return flightFareHistoryResponse;

    }

}
