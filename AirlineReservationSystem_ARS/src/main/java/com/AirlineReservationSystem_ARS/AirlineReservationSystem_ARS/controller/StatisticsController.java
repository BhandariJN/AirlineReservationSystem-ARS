package com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.controller;

import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.response.ApiResponse;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.response.ChartData;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.response.StatisticsResponse;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RequiredArgsConstructor
@RestController
@RequestMapping("/statistics")
public class StatisticsController {


    private final StatisticsService statisticsService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getFlightStatistics(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String route) {

        // If dates are not provided, use defaults (last 30 days)
        if (startDate == null) {
            startDate = LocalDate.now().minusDays(30);
        }
        if (endDate == null) {
            endDate = LocalDate.now();
        }

        StatisticsResponse statisticsData = statisticsService.generateStatistics(startDate, endDate, route);


        return ResponseEntity.ok(new ApiResponse("Success", statisticsData));
    }

    @GetMapping("/revenue-trends")
    public ResponseEntity<ApiResponse> getRevenueTrends(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        if (startDate == null) {
            startDate = LocalDate.now().minusDays(365); // Default to last year
        }
        if (endDate == null) {
            endDate = LocalDate.now();
        }

        ChartData revenueTrends = statisticsService.getRevenueTrends(startDate, endDate);


        return ResponseEntity.ok(new ApiResponse("Success", revenueTrends));
    }

    @GetMapping("/booking-trends")
    public ResponseEntity<ApiResponse> getBookingTrends(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        if (startDate == null) {
            startDate = LocalDate.now().minusDays(365); // Default to last year
        }
        if (endDate == null) {
            endDate = LocalDate.now();
        }

        ChartData bookingTrends = statisticsService.getBookingTrends(startDate, endDate);


        return ResponseEntity.ok(new ApiResponse("Success", bookingTrends));
    }

    @GetMapping("/route-performance")
    public ResponseEntity<ApiResponse> getRoutePerformance() {
        ChartData routePerformance = statisticsService.getRoutePerformance();


        return ResponseEntity.ok(new ApiResponse("Success", routePerformance));
    }
}