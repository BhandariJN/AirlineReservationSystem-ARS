package com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.repository;

import com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS.model.Flight;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Repository
public interface StatisticsRepository extends JpaRepository<Flight, Long> {

    // Find flights within a date range with pagination
    @Query(value = "SELECT f FROM Flight f JOIN f.flightSchedule fs WHERE fs.departureTime BETWEEN :startDateTime AND :endDateTime",
            countQuery = "SELECT COUNT(f) FROM Flight f JOIN f.flightSchedule fs WHERE fs.departureTime BETWEEN :startDateTime AND :endDateTime")
    Page<Flight> findFlightsByDateRange(@Param("startDateTime") LocalDateTime startDateTime,
                                        @Param("endDateTime") LocalDateTime endDateTime,
                                        Pageable pageable);

    // Find flights by route within a date range with pagination
    @Query(value = "SELECT f FROM Flight f JOIN f.flightSchedule fs JOIN fs.routeInfo r WHERE fs.departureTime BETWEEN :startDateTime AND :endDateTime AND r.routeCode = :routeCode",
            countQuery = "SELECT COUNT(f) FROM Flight f JOIN f.flightSchedule fs JOIN fs.routeInfo r WHERE fs.departureTime BETWEEN :startDateTime AND :endDateTime AND r.routeCode = :routeCode")
    Page<Flight> findFlightsByRouteAndDateRange(@Param("routeCode") String routeCode,
                                                @Param("startDateTime") LocalDateTime startDateTime,
                                                @Param("endDateTime") LocalDateTime endDateTime,
                                                Pageable pageable);

    // Count flights by status within a date range (uses native pagination)
    @Query(value = "SELECT new map(f.flightStatus as status, COUNT(f) as count) FROM Flight f JOIN f.flightSchedule fs WHERE fs.departureTime BETWEEN :startDateTime AND :endDateTime GROUP BY f.flightStatus",
            countQuery = "SELECT COUNT(DISTINCT f.flightStatus) FROM Flight f JOIN f.flightSchedule fs WHERE fs.departureTime BETWEEN :startDateTime AND :endDateTime")
    Page<Object[]> countFlightsByStatus(@Param("startDateTime") LocalDateTime startDateTime,
                                        @Param("endDateTime") LocalDateTime endDateTime,
                                        Pageable pageable);

    // Sum revenue by date within a date range with pagination
    @Query(value = "SELECT new map(FUNCTION('DATE', fs.departureTime) as date, SUM(fs.baseFare * f.seatsBooked) as revenue) FROM Flight f JOIN f.flightSchedule fs WHERE fs.departureTime BETWEEN :startDateTime AND :endDateTime GROUP BY FUNCTION('DATE', fs.departureTime) ORDER BY date",
            countQuery = "SELECT COUNT(DISTINCT FUNCTION('DATE', fs.departureTime)) FROM Flight f JOIN f.flightSchedule fs WHERE fs.departureTime BETWEEN :startDateTime AND :endDateTime")
    Page<Object[]> sumRevenueByDate(@Param("startDateTime") LocalDateTime startDateTime,
                                    @Param("endDateTime") LocalDateTime endDateTime,
                                    Pageable pageable);

    // Sum revenue by route within a date range with pagination
    @Query(value = "SELECT new map(r.routeCode as route, SUM(fs.baseFare * f.seatsBooked) as revenue) FROM Flight f JOIN f.flightSchedule fs JOIN fs.routeInfo r WHERE fs.departureTime BETWEEN :startDateTime AND :endDateTime GROUP BY r.routeCode ORDER BY revenue DESC",
            countQuery = "SELECT COUNT(DISTINCT r.routeCode) FROM Flight f JOIN f.flightSchedule fs JOIN fs.routeInfo r WHERE fs.departureTime BETWEEN :startDateTime AND :endDateTime")
    Page<Object[]> sumRevenueByRoute(@Param("startDateTime") LocalDateTime startDateTime,
                                     @Param("endDateTime") LocalDateTime endDateTime,
                                     Pageable pageable);

    // Get seat occupancy for flights within a date range with pagination
    @Query(value = "SELECT new map(f.flightNumber as flightNumber, (f.seatsBooked * 100.0 / a.capacity) as occupancyRate) FROM Flight f JOIN f.flightSchedule fs JOIN fs.airbus a WHERE fs.departureTime BETWEEN :startDateTime AND :endDateTime ORDER BY occupancyRate DESC",
            countQuery = "SELECT COUNT(f) FROM Flight f JOIN f.flightSchedule fs WHERE fs.departureTime BETWEEN :startDateTime AND :endDateTime")
    Page<Object[]> getFlightOccupancyRates(@Param("startDateTime") LocalDateTime startDateTime,
                                           @Param("endDateTime") LocalDateTime endDateTime,
                                           Pageable pageable);

    @Query(value = """
    SELECT new map(
        f.flightNumber as flightNumber, 
        CONCAT(r.origin, ' - ', r.destination) as route, 
        SUM(fs.baseFare * f.seatsBooked) as revenue, 
        (SUM(f.seatsBooked) * 100.0 / a.capacity) as occupancyRate, 
        AVG(fs.baseFare) as averageFare
    ) 
    FROM Flight f 
    JOIN f.flightSchedule fs 
    JOIN fs.routeInfo r 
    JOIN fs.airbus a 
    WHERE fs.departureTime BETWEEN :startDateTime AND :endDateTime 
    GROUP BY f.flightNumber, r.origin, r.destination, a.capacity
    """,
            countQuery = """
    SELECT COUNT(DISTINCT f.flightNumber) 
    FROM Flight f 
    JOIN f.flightSchedule fs 
    WHERE fs.departureTime BETWEEN :startDateTime AND :endDateTime
    """)
    Page<Object[]> getTopFlightsByRevenue(@Param("startDateTime") LocalDateTime startDateTime,
                                          @Param("endDateTime") LocalDateTime endDateTime,
                                          Pageable pageable);


    // Total revenue for a given period (doesn't need pagination)
    @Query("SELECT COALESCE(SUM(fs.baseFare * f.seatsBooked), 0) FROM Flight f JOIN f.flightSchedule fs WHERE fs.departureTime BETWEEN :startDateTime AND :endDateTime")
    BigDecimal calculateTotalRevenue(@Param("startDateTime") LocalDateTime startDateTime,
                                     @Param("endDateTime") LocalDateTime endDateTime);

    // Count flights for a given period (doesn't need pagination)
    @Query("SELECT COUNT(f) FROM Flight f JOIN f.flightSchedule fs WHERE fs.departureTime BETWEEN :startDateTime AND :endDateTime")
    int countFlightsByDateRange(@Param("startDateTime") LocalDateTime startDateTime,
                                @Param("endDateTime") LocalDateTime endDateTime);

    // Calculate average booking rate (doesn't need pagination)
    @Query("SELECT AVG(f.seatsBooked * 100.0 / a.capacity) FROM Flight f JOIN f.flightSchedule fs JOIN fs.airbus a WHERE fs.departureTime BETWEEN :startDateTime AND :endDateTime")
    Double calculateAverageBookingRate(@Param("startDateTime") LocalDateTime startDateTime,
                                       @Param("endDateTime") LocalDateTime endDateTime);



// Calculate average fare (doesn't need pagination)
@Query("SELECT AVG(fs.baseFare) FROM Flight f JOIN f.flightSchedule fs WHERE fs.departureTime BETWEEN :startDateTime AND :endDateTime")
BigDecimal calculateAverageFare(@Param("startDateTime") LocalDateTime startDateTime,
                                @Param("endDateTime") LocalDateTime endDateTime);
}