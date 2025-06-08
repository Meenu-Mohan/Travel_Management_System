// ReservationDetailsRepository.java
package com.ibsplc.flightreservation.repository;

import com.ibsplc.flightreservation.entity.ReservationDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface ReservationDetailsRepository extends JpaRepository<ReservationDetails, Integer> {
    
    @Query(value = "SELECT seat_number FROM reservation.reservation_details WHERE flight_id = :flightId", nativeQuery = true)
    List<String> findSeatNumbersByFlightId(String flightId);
}