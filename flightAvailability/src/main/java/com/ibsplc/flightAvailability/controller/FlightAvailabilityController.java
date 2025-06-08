package com.ibsplc.flightAvailability.controller;

import com.ibsplc.flightAvailability.entity.Flight;
import com.ibsplc.flightAvailability.service.FlightAvailabilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/availability")
public class FlightAvailabilityController {

    @Autowired
    private FlightAvailabilityService flightAvailabilityService;

    @GetMapping("/search")
    public ResponseEntity<List<Flight>> searchFlights(
            @RequestParam LocalDateTime departureDate,
            @RequestParam LocalDateTime arrivalDate,
            @RequestParam int numberOfTickets,
            @RequestParam String depAirport,
            @RequestParam String arrAirport) {
    	//logger.info("Received search request: departureDate={}, arrivalDate={}, numberOfTickets={}, depAirport={}, arrAirport={}",
                //departureDate, arrivalDate, numberOfTickets, depAirport, arrAirport);
        List<Flight> flights = flightAvailabilityService.searchFlights(departureDate, arrivalDate, numberOfTickets, depAirport, arrAirport);
        if (flights.isEmpty()) {
           // logger.info("No flights found for the given criteria");
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(flights);
    }
}