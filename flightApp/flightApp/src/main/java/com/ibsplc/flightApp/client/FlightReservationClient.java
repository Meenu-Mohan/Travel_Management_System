// FlightReservationClient.java
package com.ibsplc.flightApp.client;

import com.ibsplc.flightApp.dto.BookingDetailsResponseDTO;
import com.ibsplc.flightApp.dto.FlightDTO;
import com.ibsplc.flightApp.dto.PassengerDetailsDTO;
import com.ibsplc.flightApp.dto.ReservationDetailsDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@FeignClient(name = "flightReservation")
public interface FlightReservationClient {

    @PostMapping("/api/reservations/book")
    ReservationDetailsDTO bookFlight(
            @RequestParam("flightId") String flightId,
            @RequestParam("numberOfTickets") int numberOfTickets,
            @RequestParam("reservedClass") String reservedClass,
            @RequestBody List<PassengerDetailsDTO> passengerDetailsList);

    @GetMapping("/api/reservations/{reservationId}")
    BookingDetailsResponseDTO getBookingDetails(@PathVariable("reservationId") Integer reservationId);
}