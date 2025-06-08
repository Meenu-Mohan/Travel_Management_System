// FlightReservationService.java
package com.ibsplc.flightreservation.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibsplc.flightreservation.entity.Flight;
import com.ibsplc.flightreservation.entity.ReservationDetails;
import com.ibsplc.flightreservation.entity.PassengerDetails;
import com.ibsplc.flightreservation.repository.FlightRepository;
import com.ibsplc.flightreservation.repository.ReservationDetailsRepository;
import com.ibsplc.flightreservation.repository.PassengerDetailsRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class FlightReservationService {

    private static final Logger logger = LogManager.getLogger(FlightReservationService.class);
    private static final String BOOKING_QUEUE = "booking-queue";

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private ReservationDetailsRepository reservationDetailsRepository;
    
    @Autowired
    private PassengerDetailsRepository passengerDetailsRepository;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private SeatAllocator seatAllocator;

    @Autowired
    private EmailService emailService;
    
    @Autowired
    private ObjectMapper objectMapper;
    @Transactional
    public ReservationDetails bookFlight(String flightId, int numberOfTickets, String reservedClass, List<PassengerDetails> passengerDetailsList) {
        logger.info("Received bookFlight request: flightId={}, numberOfTickets={}, reservedClass={}, passengerDetailsList.size()={}",
                flightId, numberOfTickets, reservedClass, passengerDetailsList != null ? passengerDetailsList.size() : 0);

        // Validate number of passengers matches number of tickets
        if (passengerDetailsList == null || passengerDetailsList.size() != numberOfTickets) {
            logger.error("Validation failed: Number of passenger details ({}) does not match number of tickets ({})",
                    passengerDetailsList != null ? passengerDetailsList.size() : 0, numberOfTickets);
            throw new RuntimeException("Number of passenger details must match number of tickets: " + numberOfTickets);
        }

        // Check if flight exists and has enough seats
        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> {
                    logger.error("Flight not found for flightId: {}", flightId);
                    return new RuntimeException("Flight not found: " + flightId);
                });

        if (flight.getAvailableNoOfSeats() < numberOfTickets) {
            logger.error("Not enough seats available for flightId: {}. Available: {}, Requested: {}", 
                    flightId, flight.getAvailableNoOfSeats(), numberOfTickets);
            throw new RuntimeException("Not enough seats available for flight: " + flightId);
        }

        // Automatically allocate seats
        List<String> seatNumbers = seatAllocator.allocateSeats(flightId, numberOfTickets);

        // Update available seats
        int updatedRows = flightRepository.updateFlightSeats(flightId, numberOfTickets);
        if (updatedRows == 0) {
            logger.error("Failed to update seats for flightId: {}", flightId);
            throw new RuntimeException("Failed to update seats for flight: " + flightId);
        }

        // Create reservation
        ReservationDetails reservation = new ReservationDetails();
        reservation.setFlightId(flightId);
        reservation.setBookingDate(LocalDateTime.now());
        reservation.setReservationStatus("CONFIRMED");
        reservation.setReservedClass(reservedClass);
        reservation.setSeatNumber(String.join(",", seatNumbers));

        ReservationDetails savedReservation = reservationDetailsRepository.save(reservation);
        logger.info("Saved reservation with reservationId: {}", savedReservation.getReservationId());

        // Save passenger details and link them to the reservation
        for (PassengerDetails passenger : passengerDetailsList) {
            passenger.setNoOfTickets(1);
            passenger.setReservationId(savedReservation.getReservationId());
            PassengerDetails savedPassenger = passengerDetailsRepository.save(passenger);
            logger.info("Saved passenger details with passengerId: {} for reservationId: {}", 
                    savedPassenger.getPassengerId(), savedReservation.getReservationId());

            // Send email notification to each passenger
            String passengerEmail = passenger.getEmailAddress();
            if (passengerEmail == null || passengerEmail.trim().isEmpty()) {
                logger.warn("No email address provided for passengerId: {}. Skipping email notification.", 
                        savedPassenger.getPassengerId());
            } else {
                emailService.sendBookingConfirmationEmail(savedReservation, passengerEmail);
            }
        }

        // Send booking confirmation to message queue
        sendBookingConfirmation(savedReservation);

        System.out.println("Booking Confirmed! Reservation ID: " + savedReservation.getReservationId() + 
                          ", Flight ID: " + flightId + 
                          ", Number of Tickets: " + numberOfTickets + 
                          ", Seats: " + String.join(",", seatNumbers));

        return savedReservation;
    }

    private void sendBookingConfirmation(ReservationDetails reservation) {
        try {
            // Convert ReservationDetails to JSON string
            String reservationJson = objectMapper.writeValueAsString(reservation);
            jmsTemplate.convertAndSend(BOOKING_QUEUE, reservationJson);
            logger.info("Sent booking confirmation to queue for reservationId: {}", 
                       reservation.getReservationId());
        } catch (Exception e) {
            logger.error("Failed to send booking confirmation to queue: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to send booking confirmation to queue", e);
        }
    }

    @JmsListener(destination = BOOKING_QUEUE)
    public void processBookingConfirmation(String reservationJson) {
        try {
            // Convert JSON string back to ReservationDetails
            ReservationDetails reservation = objectMapper.readValue(reservationJson, ReservationDetails.class);
            logger.info("Processing booking confirmation from queue for reservationId: {}", 
                       reservation.getReservationId());
      
        } catch (Exception e) {
            logger.error("Failed to process booking confirmation from queue: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to process booking confirmation from queue", e);
        }
    }

    public ReservationDetails getBookingDetails(Integer reservationId) {
        logger.info("Retrieving booking details for reservationId: {}", reservationId);
        ReservationDetails reservation = reservationDetailsRepository.findById(reservationId)
                .orElseThrow(() -> {
                    logger.error("Reservation not found for reservationId: {}", reservationId);
                    return new RuntimeException("Reservation not found: " + reservationId);
                });
        logger.info("Found reservation: {}", reservation);
        return reservation;
    }

    public List<PassengerDetails> getPassengersByReservationId(Integer reservationId) {
        logger.info("Retrieving passengers for reservationId: {}", reservationId);
        List<PassengerDetails> passengers = passengerDetailsRepository.findByReservationId(reservationId);
        logger.info("Found {} passengers for reservationId: {}", passengers.size(), reservationId);
        return passengers;
    }
}