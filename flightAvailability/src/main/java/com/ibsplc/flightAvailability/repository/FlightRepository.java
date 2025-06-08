package com.ibsplc.flightAvailability.repository;

import com.ibsplc.flightAvailability.entity.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

public interface FlightRepository extends JpaRepository<Flight, String> {
    
    @Query(value = "SELECT * FROM inventory.flight f " +
            "WHERE DATE(f.dep_date_time) >= :departureDate " +
            "AND DATE(f.arr_date_time) <= :arrivalDate " +
            "AND f.dep_airport = :departureAirport " +
            "AND f.arr_airport = :arrivalAirport " +
            "AND f.available_no_of_seats >= :numberOfTickets",
            nativeQuery = true)
    List<Flight> findFlightsByCriteria(
            @Param("departureDate") LocalDateTime departureDate, 
            @Param("arrivalDate") LocalDateTime arrivalDate,
            @Param("numberOfTickets") int numberOfTickets,
            @Param("departureAirport") String depAirport, 
            @Param("arrivalAirport") String arrAirport);
}