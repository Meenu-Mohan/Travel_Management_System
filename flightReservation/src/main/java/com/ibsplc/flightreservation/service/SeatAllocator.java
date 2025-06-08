// SeatAllocator.java
package com.ibsplc.flightreservation.service;

import com.ibsplc.flightreservation.entity.Flight;
import com.ibsplc.flightreservation.repository.FlightRepository;
import com.ibsplc.flightreservation.repository.ReservationDetailsRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SeatAllocator {

    private static final Logger logger = LogManager.getLogger(SeatAllocator.class);

    @Autowired
    private ReservationDetailsRepository reservationDetailsRepository;

    @Autowired
    private FlightRepository flightRepository;

    public List<String> allocateSeats(String flightId, int numberOfTickets) {
        logger.debug("Allocating seats for flightId: {}, numberOfTickets: {}", flightId, numberOfTickets);

        // Get flight details to determine total seats
        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> {
                    logger.error("Flight not found for flightId: {}", flightId);
                    return new RuntimeException("Flight not found: " + flightId);
                });
        int totalNoOfSeats = flight.getTotalNoOfSeats();

        // Get all previously allocated seats for this flight
        List<String> allocatedSeats = reservationDetailsRepository.findSeatNumbersByFlightId(flightId)
                .stream()
                .filter(seat -> seat != null && !seat.isEmpty())
                .flatMap(seat -> Arrays.stream(seat.split(",")))
                .collect(Collectors.toList());
        logger.debug("Previously allocated seats: {}", allocatedSeats);

        // Generate all possible seats (e.g., 1A to 20F for 120 seats)
        List<String> allSeats = generateAllSeats(totalNoOfSeats);
        logger.debug("All possible seats: {}", allSeats);

        // Find available seats
        List<String> availableSeats = allSeats.stream()
                .filter(seat -> !allocatedSeats.contains(seat))
                .collect(Collectors.toList());
        logger.debug("Available seats: {}", availableSeats);

        // Check if enough seats are available
        if (availableSeats.size() < numberOfTickets) {
            logger.error("Not enough seats available for flightId: {}. Required: {}, Available: {}", 
                    flightId, numberOfTickets, availableSeats.size());
            throw new RuntimeException("Not enough seats available for flight: " + flightId);
        }

        // Allocate the first 'numberOfTickets' seats
        List<String> assignedSeats = availableSeats.subList(0, numberOfTickets);
        logger.info("Assigned seats for flightId: {}: {}", flightId, assignedSeats);
        return assignedSeats;
    }

    private List<String> generateAllSeats(int totalNoOfSeats) {
        List<String> seats = new ArrayList<>();
        int seatsPerRow = 6; // A, B, C, D, E, F
        int rows = totalNoOfSeats / seatsPerRow;
        char[] seatLetters = {'A', 'B', 'C', 'D', 'E', 'F'};

        for (int row = 1; row <= rows; row++) {
            for (char letter : seatLetters) {
                seats.add(row + String.valueOf(letter));
            }
        }
        return seats;
    }
}