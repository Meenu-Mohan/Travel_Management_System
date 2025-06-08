package com.ibsplc.flightAvailability.service;

import com.ibsplc.flightAvailability.entity.Flight;
import com.ibsplc.flightAvailability.repository.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FlightAvailabilityService {

    private static final Logger logger = LogManager.getLogger(FlightAvailabilityService.class);

    @Autowired
    private FlightRepository flightRepository;

    public List<Flight> searchFlights(LocalDateTime departureDate, LocalDateTime arrivalDate, int numberOfTickets, String depAirport, String arrAirport) {
        logger.info("Searching flights with departureDate={}, arrivalDate={}, numberOfTickets={}, depAirport={}, arrAirport={}",
                departureDate, arrivalDate, numberOfTickets, depAirport, arrAirport);
        List<Flight> flights = flightRepository.findFlightsByCriteria(departureDate, arrivalDate, numberOfTickets, depAirport, arrAirport);
        logger.info("Found {} flights", flights.size());
        return flights;
    }
}