// PassengerDetailsRepository.java
package com.ibsplc.flightreservation.repository;

import com.ibsplc.flightreservation.entity.PassengerDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PassengerDetailsRepository extends JpaRepository<PassengerDetails, Integer> {
    List<PassengerDetails> findByReservationId(Integer reservationId);
}