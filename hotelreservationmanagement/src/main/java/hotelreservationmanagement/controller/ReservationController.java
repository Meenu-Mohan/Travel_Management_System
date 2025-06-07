package hotelreservationmanagement.controller;


import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hotelreservationmanagement.dto.ReservationRequestDTO;
import hotelreservationmanagement.dto.ReservationResponseDTO;
import hotelreservationmanagement.service.ReservationService;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/hotelBooking")
public class ReservationController {

    private static final Logger logger = LogManager.getLogger(ReservationController.class);

    @Autowired
    private ReservationService reservationService;

   
    @PostMapping(value = "/hotelReservation", consumes = "application/json")
    public ResponseEntity<ReservationResponseDTO> createReservation(
            @Valid @RequestBody ReservationRequestDTO requestDTO) {
        logger.info("Received reservation request for hotel: {}", requestDTO.getHotelName());
        try {
            ReservationResponseDTO responseDTO = reservationService.createReservation(requestDTO);
            return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
        } catch (IllegalStateException e) {
            logger.warn("Invalid state for reservation: {}", e.getMessage());
            throw e; // Handled by GlobalExceptionHandler
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid reservation data: {}", e.getMessage());
            throw e; // Handled by GlobalExceptionHandler
        } catch (Exception e) {
            logger.error("Unexpected error while creating reservation", e);
            throw e; // Handled by GlobalExceptionHandler
        }
    }
    
    @GetMapping("/reservationsByGuest")
    public ResponseEntity<List<ReservationResponseDTO>> getReservationsByGuest(
            @RequestParam String email,
            @RequestParam String phone) {
        logger.info("Received request to fetch reservations for email: {} and phone: {}", email, phone);
        try {
            List<ReservationResponseDTO> reservations = reservationService.getReservationsByGuest(email, phone);
            return new ResponseEntity<>(reservations, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid request parameters: {}", e.getMessage());
            throw e; // Handled by GlobalExceptionHandler
        } catch (Exception e) {
            logger.error("Unexpected error while fetching reservations", e);
            throw e; // Handled by GlobalExceptionHandler
        }
    }
}