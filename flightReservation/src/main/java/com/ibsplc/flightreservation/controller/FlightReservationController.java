// FlightReservationController.java
package com.ibsplc.flightreservation.controller;

import com.ibsplc.flightreservation.entity.Flight;
import com.ibsplc.flightreservation.entity.PassengerDetails;
import com.ibsplc.flightreservation.entity.ReservationDetails;
import com.ibsplc.flightreservation.service.FlightReservationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class FlightReservationController {

    private static final Logger logger = LogManager.getLogger(FlightReservationController.class);

    @Autowired
    private FlightReservationService reservationService;


    @PostMapping("/book")
    public ResponseEntity<ReservationDetails> bookFlight(
            @RequestParam String flightId,
            @RequestParam int numberOfTickets,
            @RequestParam String reservedClass,
            @RequestBody List<PassengerDetails> passengerDetailsList) {
        logger.info("Received book request: flightId={}, numberOfTickets={}, reservedClass={}, passengerDetailsList.size()={}",
                flightId, numberOfTickets, reservedClass, passengerDetailsList != null ? passengerDetailsList.size() : 0);
        try {
            ReservationDetails reservation = reservationService.bookFlight(
                flightId, numberOfTickets, reservedClass, passengerDetailsList);
            return ResponseEntity.ok(reservation);
        } catch (Exception e) {
            logger.error("Booking failed: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/{reservationId}")
    public ResponseEntity<BookingDetailsResponse> getBookingDetails(@PathVariable Integer reservationId) {
        logger.info("Received request to retrieve booking details for reservationId: {}", reservationId);
        try {
            ReservationDetails reservation = reservationService.getBookingDetails(reservationId);
            List<PassengerDetails> passengers = reservationService.getPassengersByReservationId(reservationId);
            BookingDetailsResponse response = new BookingDetailsResponse(reservation, passengers);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Failed to retrieve booking details for reservationId: {}: {}", reservationId, e.getMessage(), e);
            return ResponseEntity.badRequest().body(null);
        }
    }
}