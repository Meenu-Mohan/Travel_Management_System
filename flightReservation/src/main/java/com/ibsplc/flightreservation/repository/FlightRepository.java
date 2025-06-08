// FlightRepository.java
package com.ibsplc.flightreservation.repository;

import com.ibsplc.flightreservation.entity.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface FlightRepository extends JpaRepository<Flight, String> {
    
    
    @Transactional
    @Modifying
    @Query(value = "UPDATE inventory.flight " +
            "SET available_no_of_seats = available_no_of_seats - :numberOfTickets " +
            "WHERE flight_id = :flightId " +
            "AND available_no_of_seats >= :numberOfTickets", 
    nativeQuery = true)
    int updateFlightSeats(@Param("flightId") String flightId, @Param("numberOfTickets") int numberOfTickets);
    
    @Query(value = "SELECT f.seat_rate FROM inventory.flight f WHERE f.flight_id = :flightId", 
           nativeQuery = true)
    Long getSeatRate(@Param("flightId") String flightId);
}