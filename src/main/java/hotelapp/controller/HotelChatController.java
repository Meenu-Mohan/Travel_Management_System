package hotelapp.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.List;
import org.springframework.http.HttpStatus;

import hotelapp.dto.GuestRequestDTO;
import hotelapp.dto.GuestResponseDTO;
import hotelapp.dto.HotelAndRoomsDTO;
import hotelapp.dto.ReservationRequestDTO;
import hotelapp.dto.ReservationResponseDTO;
import hotelapp.entity.Rooms;
import hotelapp.intent.IntentDetector;

@RestController
@RequestMapping("/hotelapp/chat")
public class HotelChatController {
    private static final Logger logger = LogManager.getLogger(HotelChatController.class);

    @Autowired
    private HotelappController hotelappController;

    @PostMapping
    public ResponseEntity<String> chat(@RequestBody ChatRequest chatRequest) {
        logger.info("Received chat request: {}, guestId: {}", chatRequest.getMessage(), chatRequest.getGuestId());
        try {
            IntentDetector.Intent intent = IntentDetector.detectIntent(chatRequest.getMessage());
            String response;
            switch (intent.action) {
                case "search_hotels":
                    if (intent.cityName == null || intent.checkInDate == null || intent.checkOutDate == null) {
                        logger.warn("Invalid search request: missing city or dates in message: {}", chatRequest.getMessage());
                        response = "Please provide a city and dates (e.g., 'Find hotels in New York from 04-10-2025 to 04-12-2025').";
                    } else {
                        try {
                            String city_name = intent.cityName;
                            SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
                            String checkInStr = formatter.format(intent.checkInDate);
                            String checkOutStr = formatter.format(intent.checkOutDate);
                            logger.info("Searching hotels in {} from {} to {}", city_name, checkInStr, checkOutStr);
                            List<HotelAndRoomsDTO> hotels = hotelappController.searchHotels(
                                city_name.toUpperCase(),
                                checkInStr,
                                checkOutStr
                            );
                            response = formatHotelResponse(hotels);
                        } catch (Exception e) {
                            logger.error("Error formatting dates for search request: {}", e.getMessage(), e);
                            response = "There was an issue with the dates provided. Please try again.";
                        }
                    }
                    break;
                case "book_hotel":
                    ReservationRequestDTO reservationDTO = intent.toReservationRequestDTO();
                    if (reservationDTO == null) {
                        logger.warn("Invalid booking request: unable to parse message: {}", chatRequest.getMessage());
                        response = "Please provide hotel name, room type, city, dates, email, phone, number of adults, and number of children " +
                                   "(e.g., 'Book hotel Hilton room Deluxe in New York from 04-10-2025 to 04-12-2025 email john@example.com phone +1234567890 adults 2 children 1').";
                    } else {
                        String hotel_name = reservationDTO.getHotelName();
                        String city_name = reservationDTO.getCityName();
                        String room_type = reservationDTO.getRoomType();
                        logger.info("Processing booking request for hotel: {} in city: {}", hotel_name, city_name);
                        reservationDTO.setCityName(city_name.toUpperCase());
                        reservationDTO.setHotelName(hotel_name.toUpperCase());
                        reservationDTO.setRoomType(room_type.toUpperCase());
                        reservationDTO.setStatus("CONFIRMED");
                        ResponseEntity<ReservationResponseDTO> reservationResponse = hotelappController.bookHotel(reservationDTO);
                        if (reservationResponse.getStatusCode().is2xxSuccessful() && reservationResponse.getBody() != null) {
                            logger.info("Booking successful for hotel: {}, reservation ID: {}", hotel_name, reservationResponse.getBody().getReservationId());
                            response = "Reservation successful! ID: " + reservationResponse.getBody().getReservationId();
                        } else {
                            logger.warn("Failed to create reservation for hotel: {}", hotel_name);
                            response = "Failed to create reservation. Please try again.";
                        }
                    }
                    break;
                case "register_guest":
                    GuestRequestDTO guestDTO = intent.toGuestRequestDTO();
                    if (guestDTO == null) {
                        logger.warn("Invalid registration request: unable to parse message: {}", chatRequest.getMessage());
                        response = "Please provide first name, last name, email, and phone " +
                                   "(e.g., 'Register guest name John Doe email john@example.com phone +1234567890').";
                    } else {
                        logger.info("Processing registration request for email: {}", guestDTO.getEmail());
                        ResponseEntity<?> guestResponse = hotelappController.guestRegistration(guestDTO);
                        if (guestResponse.getStatusCode().is2xxSuccessful() && guestResponse.getBody() instanceof GuestResponseDTO) {
                            GuestResponseDTO responseDTO = (GuestResponseDTO) guestResponse.getBody();
                            if (guestResponse.getStatusCode().value() == HttpStatus.CREATED.value()) {
                                logger.info("Guest registered successfully with ID: {}", responseDTO.getGuestId());
                                response = "Guest registered successfully! ID: " + responseDTO.getGuestId() +
                                           "\nNow you can book a hotel (e.g., 'Book hotel Hilton room Deluxe in New York from 04-10-2025 to 04-12-2025 email john@example.com phone +1234567890 adults 2 children 1').";
                            } else {
                                logger.info("Guest with email {} already exists with ID: {}", guestDTO.getEmail(), responseDTO.getGuestId());
                                response = "Guest already exists with ID: " + responseDTO.getGuestId() +
                                           "\nYou can book a hotel (e.g., 'Book hotel Hilton room Deluxe in New York from 04-10-2025 to 04-12-2025 email john@example.com phone +1234567890 adults 2 children 1').";
                            }
                        } else {
                            Object errorBody = guestResponse.getBody();
                            String errorMessage = "Failed to register guest.";
                            if (errorBody != null) {
                                logger.error("Failed to register guest with email {}: {}", guestDTO.getEmail(), errorBody.toString());
                                errorMessage += " " + errorBody.toString();
                            } else {
                                logger.error("Failed to register guest with email {}", guestDTO.getEmail());
                                errorMessage += " Please try again.";
                            }
                            response = errorMessage;
                        }
                    }
                    break;
                case "reservations_by_guest":
                    if (intent.email == null || intent.phone == null) {
                        logger.warn("Invalid reservations request: missing email or phone in message: {}", chatRequest.getMessage());
                        response = "Please provide your email and phone to view reservations " +
                                   "(e.g., 'View my reservations email john@example.com phone +1234567890').";
                    } else {
                        logger.info("Fetching reservations for email: {} and phone: {}", intent.email, intent.phone);
                        try {
                            ResponseEntity<List<ReservationResponseDTO>> reservationsResponse = 
                                hotelappController.getReservationsByGuest(intent.email, intent.phone);
                            if (reservationsResponse.getStatusCode().is2xxSuccessful() && reservationsResponse.getBody() != null) {
                                List<ReservationResponseDTO> reservations = reservationsResponse.getBody();
                                if (reservations.isEmpty()) {
                                    logger.info("No reservations found for email: {} and phone: {}", intent.email, intent.phone);
                                    response = "No reservations found for email: " + intent.email + " and phone: " + intent.phone + ".";
                                } else {
                                    logger.info("Found {} reservations for email: {} and phone: {}", reservations.size(), intent.email, intent.phone);
                                    StringBuilder reservationsList = new StringBuilder("Your reservations:\n");
                                    for (ReservationResponseDTO reservation : reservations) {
                                        reservationsList.append("Reservation ID: ").append(reservation.getReservationId())
                                            .append("\n Hotel: ").append(reservation.getHotelName())
                                            .append("\n Room: ").append(reservation.getRoomType())
                                            .append("\n First Name: ").append(reservation.getGuestFirstName())
                                            .append("\n Last Name: ").append(reservation.getGuestLastName())
                                            .append("\n Check-in: ").append(new SimpleDateFormat("MM-dd-yyyy").format(reservation.getCheckIn()))
                                            .append("\n Check-out: ").append(new SimpleDateFormat("MM-dd-yyyy").format(reservation.getCheckOut()))
                                            .append("\n");
                                    }
                                    response = reservationsList.toString();
                                }
                            } else {
                                logger.warn("Failed to retrieve reservations for email: {} and phone: {}", intent.email, intent.phone);
                                response = "Failed to retrieve reservations. Please try again.";
                            }
                        } catch (Exception e) {
                            logger.error("Error retrieving reservations for email {} and phone {}: {}", intent.email, intent.phone, e.getMessage(), e);
                            response = "Error retrieving reservations. Please try again.";
                        }
                    }
                    break;
                default:
                    logger.warn("Unknown intent for message: {}", chatRequest.getMessage());
                    response = "I’m not sure what you mean. You can:\n" +
                               "- Search hotels (e.g., 'Find hotels in New York from 04-10-2025 to 04-12-2025')\n" +
                               "- Book a hotel (after registration, e.g., 'Book hotel Hilton room Deluxe in New York from 04-10-2025 to 04-12-2025 email john@example.com phone +1234567890 adults 2 children 1')\n" +
                               "- Register a guest (e.g., 'Register guest name John Doe email john@example.com phone +1234567890')";
                    break;
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error processing chat request: {}", chatRequest.getMessage(), e);
            return ResponseEntity.status(500).body("Oops! Something went wrong.");
        }
    }

    private String formatHotelResponse(List<HotelAndRoomsDTO> hotels) {
        if (hotels == null || hotels.isEmpty()) {
            logger.warn("No hotels available in the search results");
            return "No hotels available.";
        }
        logger.info("Formatting response for {} hotels", hotels.size());
        StringBuilder response = new StringBuilder("Available hotels:\n");
        for (HotelAndRoomsDTO hotel : hotels) {
            // Append hotel name with a colon and newline
            response.append(hotel.getHotel().getHotelName()).append(":\n");
            // List each room on a new line with indentation
            for (Rooms room : hotel.getAvailableRooms()) {
                response.append("  - ")
                        .append(room.getRoomType())
                        .append(" (Min Capacity: ")
                        .append(room.getMinCapacity())
                        .append(", Max Capacity: ")
                        .append(room.getMaxCapacity())
                        .append(")\n");
            }
        }
        return response.toString();
    }
}

class ChatRequest {
    private String message;
    private String guestId;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getGuestId() {
        return guestId;
    }

    public void setGuestId(String guestId) {
        this.guestId = guestId;
    }
}