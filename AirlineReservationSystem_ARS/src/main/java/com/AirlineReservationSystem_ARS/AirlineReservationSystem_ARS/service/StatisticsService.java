package com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.service;

import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.enums.FlightStatus;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.repository.StatisticsRepository;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.response.ChartData;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.response.StatisticsResponse;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.response.StatisticsSummary;
import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.response.TopFlight;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class StatisticsService   {


    private final StatisticsRepository statisticsRepository;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MMM dd");

    private final Pageable pageable = Pageable.ofSize(10);


    public StatisticsResponse generateStatistics(LocalDate startDate, LocalDate endDate, String route) {
       StatisticsResponse response = new StatisticsResponse();

        // Convert LocalDate to LocalDateTime for queries
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        // Calculate for previous period of the same duration for comparison
        long daysBetween = endDate.toEpochDay() - startDate.toEpochDay() + 1;
        LocalDate prevStartDate = startDate.minusDays(daysBetween);
        LocalDate prevEndDate = endDate.minusDays(daysBetween);
        LocalDateTime prevStartDateTime = prevStartDate.atStartOfDay();
        LocalDateTime prevEndDateTime = prevEndDate.atTime(LocalTime.MAX);

        // Generate summary statistics
        response.setSummary(generateSummary(startDateTime, endDateTime, prevStartDateTime, prevEndDateTime));

        // Generate revenue over time chart
        response.setRevenueOverTime(generateRevenueOverTimeChart(startDateTime, endDateTime));

        // Generate flight status distribution chart
        response.setFlightStatusDistribution(generateFlightStatusChart(startDateTime, endDateTime));

        // Generate top routes by revenue chart
        response.setTopRoutes(generateTopRoutesChart(startDateTime, endDateTime));

        // Generate seat occupancy chart
        response.setSeatOccupancy(generateSeatOccupancyChart(startDateTime, endDateTime));

        // Generate top performing flights
        response.setTopFlights(generateTopFlights(startDateTime, endDateTime));

        return response;
    }


    public ChartData getRevenueTrends(LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        return generateRevenueOverTimeChart(startDateTime, endDateTime);
    }

    public ChartData getBookingTrends(LocalDate startDate, LocalDate endDate) {
        // This method would implement booking trend analysis
        // For simplicity, we'll return a mock implementation here
        
        return new ChartData();
    }

    public ChartData getRoutePerformance() {
        // This method would analyze route performance over all time
        // For simplicity, we'll return a mock implementation here
        return new ChartData();
    }

    // Helper method to generate summary statistics
    private StatisticsSummary generateSummary(
            LocalDateTime startDateTime,
            LocalDateTime endDateTime,
            LocalDateTime prevStartDateTime,
            LocalDateTime prevEndDateTime) {

        StatisticsSummary summary = new StatisticsSummary();

        // Calculate current period metrics
        BigDecimal totalRevenue = statisticsRepository.calculateTotalRevenue(startDateTime, endDateTime);
        int totalFlights = statisticsRepository.countFlightsByDateRange(startDateTime, endDateTime);
        Double bookingRate = statisticsRepository.calculateAverageBookingRate(startDateTime, endDateTime);
        BigDecimal avgFare = statisticsRepository.calculateAverageFare(startDateTime, endDateTime);

        // Calculate previous period metrics for comparison
        BigDecimal prevTotalRevenue = statisticsRepository.calculateTotalRevenue(prevStartDateTime, prevEndDateTime);
        int prevTotalFlights = statisticsRepository.countFlightsByDateRange(prevStartDateTime, prevEndDateTime);
        Double prevBookingRate = statisticsRepository.calculateAverageBookingRate(prevStartDateTime, prevEndDateTime);
        BigDecimal prevAvgFare = statisticsRepository.calculateAverageFare(prevStartDateTime, prevEndDateTime);

        // Set summary values
        summary.setTotalRevenue(totalRevenue != null ? totalRevenue.doubleValue() : 0);
        summary.setTotalFlights(totalFlights);
        summary.setBookingRate(bookingRate != null ? bookingRate : 0);
        summary.setAverageFare(avgFare != null ? avgFare.doubleValue() : 0);

        // Calculate growth percentages
        if (prevTotalRevenue != null && prevTotalRevenue.doubleValue() > 0) {
            assert totalRevenue != null;
            double revenueGrowth = ((totalRevenue.doubleValue() - prevTotalRevenue.doubleValue()) / prevTotalRevenue.doubleValue()) * 100;
            summary.setRevenueGrowth(revenueGrowth);
        }

        if (prevTotalFlights > 0) {
            double flightsGrowth = ((double)(totalFlights - prevTotalFlights) / prevTotalFlights) * 100;
            summary.setFlightsGrowth(flightsGrowth);
        }

        if (prevBookingRate != null && prevBookingRate > 0) {
            double bookingRateGrowth = ((bookingRate - prevBookingRate) / prevBookingRate) * 100;
            summary.setBookingRateGrowth(bookingRateGrowth);
        }
        if (prevAvgFare != null && prevAvgFare.doubleValue() > 0) {
            assert avgFare != null;
            double fareGrowth = ((avgFare.doubleValue() - prevAvgFare.doubleValue()) / prevAvgFare.doubleValue()) * 100;
            summary.setAverageFareGrowth(fareGrowth);
        }

        return summary;
    }

    // Helper method to generate revenue over time chart
    private ChartData generateRevenueOverTimeChart(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        Page<Object[]> revenueByDate = statisticsRepository.sumRevenueByDate(startDateTime, endDateTime,pageable);
        if(revenueByDate.isEmpty()){
            return null;
        }
        List<String> labels = new ArrayList<>();
        List<Number> values = new ArrayList<>();

        for (Object[] row : revenueByDate) {
            LocalDate date = (LocalDate) row[0];
            BigDecimal revenue = (BigDecimal) row[1];

            labels.add(date.format(DATE_FORMATTER));
            values.add(revenue);
        }

        return new ChartData(labels, values);
    }

    // Helper method to generate flight status distribution chart
    private ChartData generateFlightStatusChart(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        Page<Object[]> flightsByStatus = statisticsRepository.countFlightsByStatus(startDateTime, endDateTime,pageable);

        if(flightsByStatus.isEmpty()){
            return null;
        }

        List<String> labels = new ArrayList<>();
        List<Number> values = new ArrayList<>();

        // Process the flight status counts
        for (Object[] row : flightsByStatus) {
            FlightStatus status = (FlightStatus) row[0];
            Long count = (Long) row[1];

            labels.add(status.toString());
            values.add(count);
        }

        return new ChartData(labels, values);
    }

    // Helper method to generate top routes chart
    private ChartData generateTopRoutesChart(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        Page<Object[]> revenueByRoute = statisticsRepository.sumRevenueByRoute(startDateTime, endDateTime, pageable);

        if(revenueByRoute.isEmpty()) {
            return null;
        }

        // Limit to top 5 routes
        int limit = Math.min(revenueByRoute.getSize(), 5);
        List<String> labels = new ArrayList<>();
        List<Number> values = new ArrayList<>();

        for (int i = 0; i < limit; i++) {
            Object[] row = revenueByRoute.get().toArray();
            String route = (String) row[0];
            BigDecimal revenue = (BigDecimal) row[1];

            labels.add(route);
            values.add(revenue);
        }

        return new ChartData(labels, values);
    }

    // Helper method to generate seat occupancy chart
    private ChartData generateSeatOccupancyChart(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        Page<Object[]> occupancyRates = statisticsRepository.getFlightOccupancyRates(startDateTime, endDateTime, pageable);
        if(occupancyRates.isEmpty()) {
            return null;
        }
        // Limit to top 10 flights
        int limit = Math.min(occupancyRates.getSize(), 10);
        List<String> labels = new ArrayList<>();
        List<Number> values = new ArrayList<>();

        for (int i = 0; i < limit; i++) {
            Object[] row = occupancyRates.get().toArray();
            String flightNumber = (String) row[0];
            Double occupancyRate = (Double) row[1];

            labels.add(flightNumber);
            values.add(occupancyRate);
        }

        return new ChartData(labels, values);
    }

    // Helper method to generate top flights list
    private List<TopFlight> generateTopFlights(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        Page<Object[]> topFlightsData = statisticsRepository.getTopFlightsByRevenue(startDateTime, endDateTime, pageable);
        List<TopFlight> topFlights = new ArrayList<>();

        for (Object[] row : topFlightsData) {
            String flightNumber = (String) row[0];
            String route = (String) row[1];
            BigDecimal revenue = (BigDecimal) row[2];
            Double occupancyRate = (Double) row[3];
            BigDecimal averageFare = (BigDecimal) row[4];

            TopFlight flight = new TopFlight(
                    flightNumber,
                    route,
                    revenue.doubleValue(),
                    occupancyRate,
                    averageFare.doubleValue()
            );

            topFlights.add(flight);
        }

        return topFlights;
    }
}