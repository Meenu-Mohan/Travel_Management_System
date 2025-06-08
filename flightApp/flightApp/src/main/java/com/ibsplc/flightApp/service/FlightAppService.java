// FlightAppService.java
package com.ibsplc.flightApp.service;

import com.ibsplc.flightApp.client.FlightAvailabilityClient;
import com.ibsplc.flightApp.client.FlightReservationClient;
import com.ibsplc.flightApp.dto.BookingDetailsResponseDTO;
import com.ibsplc.flightApp.dto.FlightDTO;
import com.ibsplc.flightApp.dto.PassengerDetailsDTO;
import com.ibsplc.flightApp.dto.ReservationDetailsDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class FlightAppService {

    private static final Logger logger = LogManager.getLogger(FlightAppService.class);

    @Autowired
    private FlightReservationClient flightReservationClient;

    @Autowired
    private FlightAvailabilityClient flightAvailabilityClient;

    public List<FlightDTO> searchFlights(LocalDate departureDate, LocalDate arrivalDate, int numberOfTickets, String depAirport, String arrAirport) {
        logger.info("Searching flights with departureDate={}, arrivalDate={}, numberOfTickets={}, depAirport={}, arrAirport={}",
                departureDate, arrivalDate, numberOfTickets, depAirport, arrAirport);

        // Convert LocalDate to LocalDateTime for the flightReservationClient
        LocalDateTime departureDateTime = departureDate.atStartOfDay();
        LocalDateTime arrivalDateTime = arrivalDate.atTime(23, 59, 59);

        List<FlightDTO> flights = flightAvailabilityClient.searchFlights(departureDateTime, arrivalDateTime, numberOfTickets, depAirport, arrAirport);
        int flightCount = (flights != null) ? flights.size() : 0;
        logger.info("Found {} flights", flightCount);
        return flights;
    }

    public ReservationDetailsDTO bookFlight(String flightId, int numberOfTickets, String reservedClass, List<PassengerDetailsDTO> passengerDetailsList) {
        logger.info("Booking flight with flightId={}, numberOfTickets={}, reservedClass={}, passengerDetailsList.size()={}",
                flightId, numberOfTickets, reservedClass, passengerDetailsList != null ? passengerDetailsList.size() : 0);
        return flightReservationClient.bookFlight(flightId, numberOfTickets, reservedClass, passengerDetailsList);
    }

    public BookingDetailsResponseDTO getBookingDetails(Integer reservationId) {
        logger.info("Retrieving booking details for reservationId: {}", reservationId);
        return flightReservationClient.getBookingDetails(reservationId);
    }
}