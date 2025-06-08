package com.ibsplc.flightbatchmanagement.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import com.ibsplc.flightbatchmanagement.entity.Flight;
import com.ibsplc.flightbatchmanagement.exception.BatchProcessingException;

@Component
public class FlightItemProcessor implements ItemProcessor<Flight, Flight> {
    @Override
    public Flight process(Flight flight) throws Exception {
        // Validate required fields
        if (flight.getCarrierCode() == null || flight.getCarrierCode().trim().isEmpty()) {
            throw new BatchProcessingException("Carrier code cannot be empty");
        }
        if (flight.getFlightNumber() <= 0) {
            throw new BatchProcessingException("Flight number must be positive");
        }
        if (flight.getDepDateTime() == null) {
            throw new BatchProcessingException("Departure date and time cannot be empty");
        }
        if (flight.getArrDateTime() == null) {
            throw new BatchProcessingException("Arrival date and time cannot be empty");
        }
        if (flight.getDepAirport() == null || flight.getDepAirport().trim().isEmpty()) {
            throw new BatchProcessingException("Departure airport cannot be empty");
        }
        if (flight.getArrAirport() == null || flight.getArrAirport().trim().isEmpty()) {
            throw new BatchProcessingException("Arrival airport cannot be empty");
        }
        if (flight.getTotalNoOfSeats() <= 0) {
            throw new BatchProcessingException("Total number of seats must be positive");
        }
        if (flight.getSectorType() == null || flight.getSectorType().trim().isEmpty()) {
            throw new BatchProcessingException("Sector type cannot be empty");
        }
        if (flight.getSeatRate() <= 0) {
            throw new BatchProcessingException("Seat rate must be positive");
        }
        if (flight.getAvailableNoOfSeats() < 0) {
            throw new BatchProcessingException("Available number of seats cannot be negative");
        }

        // Normalize data (e.g., trim and convert to uppercase)
        flight.setCarrierCode(flight.getCarrierCode().trim().toUpperCase());
        flight.setDepAirport(flight.getDepAirport().trim().toUpperCase());
        flight.setArrAirport(flight.getArrAirport().trim().toUpperCase());
        flight.setSectorType(flight.getSectorType().trim().toUpperCase());

        // No need to set createdBy/updatedBy; they are handled by @PrePersist/@PreUpdate
        return flight;
    }
}