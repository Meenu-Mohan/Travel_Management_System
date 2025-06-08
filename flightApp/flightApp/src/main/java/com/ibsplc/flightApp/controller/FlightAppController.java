// FlightAppController.java
package com.ibsplc.flightApp.controller;

import com.ibsplc.flightApp.dto.BookingDetailsResponseDTO;
import com.ibsplc.flightApp.dto.FlightDTO;
import com.ibsplc.flightApp.dto.PassengerDetailsDTO;
import com.ibsplc.flightApp.dto.ReservationDetailsDTO;
import com.ibsplc.flightApp.service.FlightAppService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/flights")
public class FlightAppController {

    private static final Logger logger = LogManager.getLogger(FlightAppController.class);

    @Autowired
    private FlightAppService flightAppService;

    @GetMapping("/search")
    public ResponseEntity<List<FlightDTO>> searchFlights(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate departureDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate arrivalDate,
            @RequestParam int numberOfTickets,
            @RequestParam String depAirport,
            @RequestParam String arrAirport) {
        logger.info("Received search request: departureDate={}, arrivalDate={}, numberOfTickets={}, depAirport={}, arrAirport={}",
                departureDate, arrivalDate, numberOfTickets, depAirport, arrAirport);
        List<FlightDTO> flights = flightAppService.searchFlights(departureDate, arrivalDate, numberOfTickets, depAirport, arrAirport);
        if (flights==null) {
            logger.info("No flights found for the given criteria");
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(flights);
    }

    @PostMapping("/book")
    public ResponseEntity<ReservationDetailsDTO> bookFlight(
            @RequestParam String flightId,
            @RequestParam int numberOfTickets,
            @RequestParam String reservedClass,
            @RequestBody List<PassengerDetailsDTO> passengerDetailsList) {
        logger.info("Received book request: flightId={}, numberOfTickets={}, reservedClass={}, passengerDetailsList.size()={}",
                flightId, numberOfTickets, reservedClass, passengerDetailsList != null ? passengerDetailsList.size() : 0);
        try {
            ReservationDetailsDTO reservation = flightAppService.bookFlight(
                flightId, numberOfTickets, reservedClass, passengerDetailsList);
            return ResponseEntity.ok(reservation);
        } catch (Exception e) {
            logger.error("Booking failed: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/{reservationId}")
    public ResponseEntity<BookingDetailsResponseDTO> getBookingDetails(@PathVariable Integer reservationId) {
        logger.info("Received request to retrieve booking details for reservationId: {}", reservationId);
        try {
            BookingDetailsResponseDTO bookingDetails = flightAppService.getBookingDetails(reservationId);
            return ResponseEntity.ok(bookingDetails);
        } catch (Exception e) {
            logger.error("Failed to retrieve booking details for reservationId: {}: {}", reservationId, e.getMessage(), e);
            return  ResponseEntity.noContent().build();
        }
    }
}