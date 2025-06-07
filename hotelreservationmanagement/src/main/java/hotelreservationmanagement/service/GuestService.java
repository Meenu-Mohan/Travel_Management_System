package hotelreservationmanagement.service;

import hotelreservationmanagement.dto.GuestDTO;
import hotelreservationmanagement.entity.Guests;
import hotelreservationmanagement.repository.GuestsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Service
public class GuestService {

    private static final Logger logger = LogManager.getLogger(GuestService.class);

    @Autowired
    private GuestsRepository guestsRepository;

    @Transactional
    public GuestDTO createGuest(GuestDTO guestDTO) {
        logger.info("Creating guest: {} {}", guestDTO.getFirstName(), guestDTO.getLastName());

        // Validate input data
        if (guestDTO.getEmail() == null || guestDTO.getEmail().trim().isEmpty()) {
            logger.warn("Invalid guest email: null or empty");
            throw new IllegalArgumentException("Guest email cannot be null or empty");
        }
        if (guestDTO.getPhone() == null || guestDTO.getPhone().trim().isEmpty()) {
            logger.warn("Invalid guest phone: null or empty");
            throw new IllegalArgumentException("Guest phone cannot be null or empty");
        }
        if (!isValidEmail(guestDTO.getEmail())) {
            logger.warn("Invalid email format: {}", guestDTO.getEmail());
            throw new IllegalArgumentException("Invalid email format");
        }

        // Check if a guest with the same email and phone already exists
        Optional<Guests> existingGuest = guestsRepository.findByEmailAndPhone(guestDTO.getEmail(), guestDTO.getPhone());
        
        if (existingGuest.isPresent()) {
            logger.info("Guest with email: {} and phone: {} already exists with ID: {}", 
                guestDTO.getEmail(), guestDTO.getPhone(), existingGuest.get().getGuestId());
            // Return existing guest instead of throwing an exception
            guestDTO.setGuestId(existingGuest.get().getGuestId());
            return guestDTO;
        }

        // If no existing guest is found, proceed with creation
        Guests guest = new Guests();
        guest.setFirstName(guestDTO.getFirstName());
        guest.setLastName(guestDTO.getLastName());
        guest.setEmail(guestDTO.getEmail());
        guest.setPhone(guestDTO.getPhone());
        guest.setCreateTimestamp(LocalDateTime.now());
        guest.setUpdateTimestamp(LocalDateTime.now());
        guest.setCreatedBy("system");
        guest.setModifiedBy("system");

        try {
            guestsRepository.save(guest);
        } catch (Exception e) {
            logger.error("Failed to save guest to database", e);
            throw e; // Propagates to GlobalExceptionHandler for generic handling
        }

        guestDTO.setGuestId(guest.getGuestId());
        logger.info("Guest created successfully: {}", guest.getGuestId());
        return guestDTO;
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email != null && email.matches(emailRegex);
    }
}