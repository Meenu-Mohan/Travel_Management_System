package hotelreservationmanagement.controller;

import hotelreservationmanagement.dto.GuestDTO;
import hotelreservationmanagement.service.GuestService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@RestController
@RequestMapping("/guest")
public class GuestController {

    private static final Logger logger = LogManager.getLogger(GuestController.class);

    @Autowired
    private GuestService guestService;
    
    @PostMapping("/guestRegistration")
    public ResponseEntity<GuestDTO> createGuest(@Valid @RequestBody GuestDTO guestDTO) {
        logger.info("Received request to create guest: {} {}", guestDTO.getFirstName(), guestDTO.getLastName());
        try {
            GuestDTO createdGuest = guestService.createGuest(guestDTO);
            return new ResponseEntity<>(createdGuest, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid guest data provided: {}", e.getMessage());
            throw e; // Handled by GlobalExceptionHandler
        } catch (Exception e) {
            logger.error("Unexpected error while creating guest", e);
            throw e; // Handled by GlobalExceptionHandler
        }
    }
}