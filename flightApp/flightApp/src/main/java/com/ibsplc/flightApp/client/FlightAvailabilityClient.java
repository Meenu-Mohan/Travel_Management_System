package com.ibsplc.flightApp.client;

import com.ibsplc.flightApp.dto.FlightDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@FeignClient(name = "flightAvailability")
public interface FlightAvailabilityClient {

    @GetMapping("/api/availability/search")
    List<FlightDTO> searchFlights(
            @RequestParam("departureDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime departureDate,
            @RequestParam("arrivalDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime arrivalDate,
            @RequestParam("numberOfTickets") int numberOfTickets,
            @RequestParam("depAirport") String depAirport,
            @RequestParam("arrAirport") String arrAirport);
}
