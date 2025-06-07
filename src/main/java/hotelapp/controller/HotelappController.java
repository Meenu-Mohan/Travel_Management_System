package hotelapp.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import feign.FeignException;
import hotelapp.client.HotelAvailabilityClient;
import hotelapp.client.HotelGuestRegistrationClient;
import hotelapp.client.HotelReservationClient;
import hotelapp.dto.GuestRequestDTO;
import hotelapp.dto.GuestResponseDTO;
import hotelapp.dto.HotelAndRoomsDTO;
import hotelapp.dto.ReservationRequestDTO;
import hotelapp.dto.ReservationResponseDTO;
import hotelapp.entity.Guests;
import hotelapp.repo.GuestsRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/hotelapp")
public class HotelappController {

    private static final Logger logger = LogManager.getLogger(HotelappController.class);

    @Autowired
    private HotelAvailabilityClient hotelappClient;

    @Autowired
    private HotelReservationClient hotelReservationClient;

    @Autowired
    private HotelGuestRegistrationClient hotelGuestRegistrationClient;

    @Autowired
    private GuestsRepository guestsRepository;

    @GetMapping("/hotelSearch")
    public List<HotelAndRoomsDTO> searchHotels(
            @RequestParam String cityName,
            @RequestParam String checkInDate,
            @RequestParam String checkOutDate) {

        logger.info("Searching hotels in {} from {} to {}", cityName, checkInDate, checkOutDate);

        return hotelappClient.findAvailableHotelsWithRooms(cityName, checkInDate, checkOutDate);
    }

    @PostMapping("/hotelReservation")
    public ResponseEntity<ReservationResponseDTO> bookHotel(@Valid @RequestBody ReservationRequestDTO requestDTO) {

        logger.info("Received booking request for hotel: {} in city: {}", requestDTO.getHotelName(),
                requestDTO.getCityName());

        // Validate check-in is before check-out
        if (requestDTO.getCheckIn().after(requestDTO.getCheckOut())) {
            logger.warn("Invalid dates: Check-in {} is after check-out {}", requestDTO.getCheckIn(),
                    requestDTO.getCheckOut());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Check-in date must be before check-out date.");
        }

        // Check availability from hotel availability service
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
        String checkInDate = formatter.format(requestDTO.getCheckIn());
        String checkOutDate = formatter.format(requestDTO.getCheckOut());

        List<HotelAndRoomsDTO> availableHotels;

        try {
            availableHotels = hotelappClient.findAvailableHotelsWithRooms(requestDTO.getCityName(), checkInDate,
                    checkOutDate);

            if (availableHotels == null) {
                logger.warn("Hotel availability service returned null for city: {} from {} to {}",
                        requestDTO.getCityName(), checkInDate, checkOutDate);
                throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                        "Hotel availability service returned an invalid response.");
            }

        } catch (FeignException e) {
            logger.error("Failed to check availability for city {}: {}", requestDTO.getCityName(), e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                    "Hotel availability service is unavailable.");
        }

        // Validate availability result
        if (availableHotels.isEmpty()) {
            logger.warn("No hotels available in city {} for dates {} to {}", requestDTO.getCityName(), checkInDate,
                    checkOutDate);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "No hotels available for the selected dates in the specified city.");
        }

        // Validate hotel and room availability
        boolean isAvailable = availableHotels.stream()
                .anyMatch(hotelDTO -> hotelDTO.getHotel().getHotelName().equalsIgnoreCase(requestDTO.getHotelName())
                        && hotelDTO.getAvailableRooms().stream()
                                .anyMatch(room -> room.getRoomType().equalsIgnoreCase(requestDTO.getRoomType())));

        if (!isAvailable) {
            logger.warn("Requested hotel '{}' or room '{}' is not available in city '{}' for dates {} to {}",
                    requestDTO.getHotelName(), requestDTO.getRoomType(), requestDTO.getCityName(), checkInDate,
                    checkOutDate);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Hotel or room not available for the selected dates.");
        }

        // Pass request to hotel reservation management service
        ReservationResponseDTO responseDTO;
        try {
            responseDTO = hotelReservationClient.createReservation(requestDTO);
        } catch (FeignException e) {
            logger.error("Failed to create reservation: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Reservation service is unavailable.");
        }

        logger.info("Reservation created successfully for hotel: {} in city: {}", requestDTO.getHotelName(),
                requestDTO.getCityName());

        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @PostMapping("/guestRegistration")
    public ResponseEntity<?> guestRegistration(@Valid @RequestBody GuestRequestDTO guestRequesttDTO) {

        logger.info("Received guest registration request for email: {}", guestRequesttDTO.getEmail());

        // Check if email already exists
        Optional<Guests> existingGuestByEmail = guestsRepository.findByEmail(guestRequesttDTO.getEmail());
        if (existingGuestByEmail.isPresent()) {
            logger.info("Guest with email {} already exists", guestRequesttDTO.getEmail());
            Guests guest = existingGuestByEmail.get();
            GuestResponseDTO responseDTO = new GuestResponseDTO();
            responseDTO.setGuestId(guest.getGuestId());
            responseDTO.setFirstName(guest.getFirstName());
            responseDTO.setLastName(guest.getLastName());
            responseDTO.setEmail(guest.getEmail());
            responseDTO.setPhone(guest.getPhone());
            return new ResponseEntity<>(responseDTO, HttpStatus.OK); // 200 OK for existing guest
        }

        // Check if phone number already exists
        Optional<Guests> existingGuestByPhone = guestsRepository.findByPhone(guestRequesttDTO.getPhone());
        if (existingGuestByPhone.isPresent()) {
            logger.info("Guest with phone {} already exists", guestRequesttDTO.getPhone());
            Guests guest = existingGuestByPhone.get();
            GuestResponseDTO responseDTO = new GuestResponseDTO();
            responseDTO.setGuestId(guest.getGuestId());
            responseDTO.setFirstName(guest.getFirstName());
            responseDTO.setLastName(guest.getLastName());
            responseDTO.setEmail(guest.getEmail());
            responseDTO.setPhone(guest.getPhone());
            return new ResponseEntity<>(responseDTO, HttpStatus.OK); // 200 OK for existing guest
        }

        GuestResponseDTO guestResponseDTO;
        try {
            guestResponseDTO = hotelGuestRegistrationClient.guestRegistration(guestRequesttDTO);
        } catch (FeignException e) {
            logger.error("Failed to register guest with email {}: {}", guestRequesttDTO.getEmail(), e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Registration service is unavailable.");
        }

        logger.info("Guest registered successfully with email: {}", guestRequesttDTO.getEmail());

        return new ResponseEntity<>(guestResponseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/reservationsByGuest")
    public ResponseEntity<List<ReservationResponseDTO>> getReservationsByGuest(
            @RequestParam String email,
            @RequestParam String phone) {
        logger.info("Received request to fetch reservations for email: {} and phone: {}", email, phone);
        try {
            List<ReservationResponseDTO> reservations = hotelReservationClient.getReservationsByGuest(email, phone);
            logger.info("Successfully retrieved {} reservations for email: {} and phone: {}", reservations.size(), email, phone);
            return new ResponseEntity<>(reservations, HttpStatus.OK);
        } catch (FeignException e) {
            logger.error("Failed to fetch reservations for email {} and phone {}: {}", email, phone, e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Reservation service is unavailable.");
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid request parameters: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error while fetching reservations for email {} and phone {}", email, phone, e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred.");
        }
    }
}