package hotelreservationmanagement.service;

import hotelreservationmanagement.dto.ReservationRequestDTO;
import hotelreservationmanagement.dto.ReservationResponseDTO;
import hotelreservationmanagement.entity.*;
import hotelreservationmanagement.repository.*;
import jakarta.jms.TextMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.stream.Collectors;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;


@Service
public class ReservationService {

    private static final Logger logger = LogManager.getLogger(ReservationService.class);

    @Autowired
    private ReservationsRepository reservationsRepository;

    @Autowired
    private ReservationPricingRepository reservationPricingRepository;

    @Autowired
    private ReservationPlansRepository reservationPlansRepository;

    @Autowired
    private GuestPricingPlansRepository guestPricingPlansRepository;

    @Autowired
    private GuestReservationPlansRepository guestReservationPlansRepository;

    @Autowired
    private GuestsRepository guestsRepository;

    @Autowired
    private HotelsRepository hotelsRepository;

    @Autowired
    private RoomsRepository roomsRepository;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private PricingDetailsRepository pricingDetailsRepository;

    @Autowired
    private RoomAvailabilityRepository roomAvailabilityRepository;

    /**
     * Creates a reservation based on the provided request DTO.
     *
     * @param requestDTO the reservation request data transfer object
     * @return the reservation response DTO
     * @throws IllegalArgumentException if the request data is invalid
     * @throws IllegalStateException if the reservation cannot be made due to availability
     */
    @Transactional
    public ReservationResponseDTO createReservation(ReservationRequestDTO requestDTO) {
        logger.info("Starting reservation creation for email: {} and hotel: {}", 
            requestDTO.getEmail(), requestDTO.getHotelName());

        // Validate basic input
        if (requestDTO.getEmail() == null || requestDTO.getEmail().trim().isEmpty()) {
            logger.warn("Invalid email: null or empty");
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        if (requestDTO.getPhone() == null || requestDTO.getPhone().trim().isEmpty()) {
            logger.warn("Invalid phone: null or empty");
            throw new IllegalArgumentException("Phone cannot be null or empty");
        }

        // Fetch the guest using email and phone
        Guests guest = guestsRepository.findByEmailOrPhone(requestDTO.getEmail(), requestDTO.getPhone())
                .orElseThrow(() -> {
                    logger.warn("Guest not found with email: {} and phone: {}", requestDTO.getEmail(), requestDTO.getPhone());
                    return new IllegalArgumentException("Guest not found with provided email and phone");
                });

        // Fetch the hotel using the hotel name
        Hotels hotels = hotelsRepository.findByHotelName(requestDTO.getHotelName())
                .orElseThrow(() -> {
                    logger.warn("Hotel not found: {}", requestDTO.getHotelName());
                    return new IllegalArgumentException("Hotel not found with name: " + requestDTO.getHotelName());
                });

        // Fetch the room using the room type within the found hotel
        Rooms rooms = roomsRepository.findByRoomTypeAndHotelId(requestDTO.getRoomType(), hotels.getHotelId())
                .orElseThrow(() -> {
                    logger.warn("Room not found: {} in hotel: {}", requestDTO.getRoomType(), requestDTO.getHotelName());
                    return new IllegalArgumentException("Room type " + requestDTO.getRoomType() + " not found in hotel: " + requestDTO.getHotelName());
                });
        
     // Validate room capacity
        int totalGuests = requestDTO.getNoOfAdult() + (requestDTO.getNoOfChild() != null ? requestDTO.getNoOfChild() : 0);
        if (totalGuests > rooms.getMaxCapacity()) {
            logger.warn("Total guests ({}) exceed max capacity ({}) for room type: {} in hotel: {}",
                totalGuests, rooms.getMaxCapacity(), requestDTO.getRoomType(), requestDTO.getHotelName());
            throw new IllegalArgumentException("Total number of guests (" + totalGuests + ") exceeds room max capacity (" + rooms.getMaxCapacity() + ")");
        }
 

        // Create and save the reservation
        Reservations reservation = new Reservations();
        reservation.setHotelId(hotels.getHotelId());
        reservation.setRoomId(rooms.getRoomId());
        reservation.setGuest(guest);
        reservation.setCheckIn(requestDTO.getCheckIn());
        reservation.setCheckOut(requestDTO.getCheckOut());
        reservation.setNoOfAdult(requestDTO.getNoOfAdult());
        reservation.setNoOfChild(requestDTO.getNoOfChild());
        reservation.setStatus(requestDTO.getStatus());
        reservation.setCreateTimestamp(LocalDateTime.now());
        reservation.setUpdateTimestamp(LocalDateTime.now());
        reservation.setCreatedBy("system");
        reservation.setModifiedBy("system");

        Reservations savedReservation;
        try {
            savedReservation = reservationsRepository.save(reservation);
            logger.info("Reservation saved with ID: {}", savedReservation.getReservationId());
        } catch (Exception e) {
            logger.error("Failed to save reservation to database", e);
            throw e; // Propagates to GlobalExceptionHandler
        }

        // Price calculation
        LocalDate checkInDate = requestDTO.getCheckIn().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate checkOutDate = requestDTO.getCheckOut().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        long nights = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        if (nights < 1) {
            logger.warn("Invalid dates: check-out {} is not after check-in {}", checkOutDate, checkInDate);
            throw new IllegalArgumentException("Check-out date must be after check-in date");
        }

        LocalDate reservationDate = LocalDate.now();
        long daysUntilCheckIn = ChronoUnit.DAYS.between(reservationDate, checkInDate);
        if (daysUntilCheckIn < 0) {
            logger.warn("Check-in date {} is in the past", checkInDate);
            throw new IllegalArgumentException("Check-in date cannot be in the past");
        }

        String planId = (daysUntilCheckIn >= 21) ? "PL2" : "PL1";
        logger.info("Applying plan {} as booking is {} days before check-in", planId, daysUntilCheckIn);

        // Getting pricing details using the determined plan ID
        PricingDetails pricingDetails = pricingDetailsRepository.findByRoomIdAndPlanId(rooms.getRoomId(), planId)
                .orElseThrow(() -> {
                    logger.warn("Pricing details not found for room: {} and plan: {}", rooms.getRoomId(), planId);
                    return new IllegalArgumentException("No pricing available for room type and selected plan");
                });

        // Calculate the total price
        BigDecimal basePrice = pricingDetails.getPrice();
        BigDecimal totalPrice = basePrice.multiply(BigDecimal.valueOf(nights));
        logger.info("Calculated total price: {} for {} nights", totalPrice, nights);

        // Update RoomAvailability
        updateRoomAvailability(requestDTO);

        // Create and save reservation pricing
        ReservationPricing reservationPricing = new ReservationPricing();
        reservationPricing.setReservation(savedReservation);
        reservationPricing.setPrice(totalPrice);
        reservationPricing.setCreateTimestamp(LocalDateTime.now());
        reservationPricing.setUpdateTimestamp(LocalDateTime.now());
        reservationPricing.setCreatedBy("system");
        reservationPricing.setModifiedBy("system");
        reservationPricingRepository.save(reservationPricing);

        // Create and save reservation plans with dynamic plan ID
        ReservationPlans reservationPlans = new ReservationPlans();
        reservationPlans.setReservation(savedReservation);
        reservationPlans.setPlanId(planId);
        reservationPlans.setCreateTimestamp(LocalDateTime.now());
        reservationPlans.setUpdateTimestamp(LocalDateTime.now());
        reservationPlans.setCreatedBy("system");
        reservationPlans.setModifiedBy("system");
        reservationPlansRepository.save(reservationPlans);

        // Create and save guest pricing plans with dynamic plan ID
        GuestPricingPlans guestPricingPlans = new GuestPricingPlans();
        guestPricingPlans.setGuest(guest);
        guestPricingPlans.setPlanId(planId);
        guestPricingPlans.setCreateTimestamp(LocalDateTime.now());
        guestPricingPlans.setUpdateTimestamp(LocalDateTime.now());
        guestPricingPlans.setCreatedBy("system");
        guestPricingPlans.setModifiedBy("system");
        guestPricingPlansRepository.save(guestPricingPlans);

        // Create and save guest reservation plans
        GuestReservationPlans guestReservationPlans = new GuestReservationPlans();
        guestReservationPlans.setGuest(guest);
        guestReservationPlans.setReservationPlan(reservationPlans);
        guestReservationPlans.setCreateTimestamp(LocalDateTime.now());
        guestReservationPlans.setUpdateTimestamp(LocalDateTime.now());
        guestReservationPlans.setCreatedBy("system");
        guestReservationPlans.setModifiedBy("system");
        guestReservationPlansRepository.save(guestReservationPlans);

        // Build and return the response DTO
        ReservationResponseDTO responseDTO = new ReservationResponseDTO();
        responseDTO.setReservationId(savedReservation.getReservationId());
        responseDTO.setHotelName(hotels.getHotelName());
        responseDTO.setRoomType(rooms.getRoomId());
        responseDTO.setGuestFirstName(guest.getFirstName());
        responseDTO.setGuestLastName(guest.getLastName());
        responseDTO.setCheckIn(savedReservation.getCheckIn());
        responseDTO.setCheckOut(savedReservation.getCheckOut());
        responseDTO.setNoOfAdult(savedReservation.getNoOfAdult());
        responseDTO.setNoOfChild(savedReservation.getNoOfChild());
        responseDTO.setPlanName(planId.equals("PL1") ? "REGULAR PRICING PLAN" : "DISCOUNT FOR EARLY BOOKINGS");
        responseDTO.setStatus(savedReservation.getStatus());
        responseDTO.setPrice(reservationPricing.getPrice());

        // Send reservation confirmation message to ActiveMQ queue
        if ("Confirmed".equalsIgnoreCase(savedReservation.getStatus())) {
            sendReservationConfirmationMessage(guest.getEmail(), responseDTO);
        }

        logger.info("Reservation created successfully for reservation ID: {}", savedReservation.getReservationId());
        return responseDTO;
    }
    
    @Transactional(readOnly = true)
    public List<ReservationResponseDTO> getReservationsByGuest(String email, String phone) {
        logger.info("Fetching reservations for guest with email: {} and phone: {}", email, phone);
 
        if (email == null || email.trim().isEmpty()) {
            logger.warn("Invalid email: null or empty");
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        if (phone == null || phone.trim().isEmpty()) {
            logger.warn("Invalid phone: null or empty");
            throw new IllegalArgumentException("Phone cannot be null or empty");
        }
 
        // Fetch the guest to verify existence
        Guests guest = guestsRepository.findByEmailAndPhone(email, phone)
                .orElseThrow(() -> {
                    logger.warn("Guest not found with email: {} and phone: {}", email, phone);
                    return new IllegalArgumentException("Guest not found with provided email and phone");
                });
 
        // Fetch all reservations for the guest
        List<Reservations> reservations = reservationsRepository.findByGuestEmailAndPhone(email, phone);
 
        // Map reservations to response DTOs
        return reservations.stream().map(reservation -> {
            Hotels hotel = hotelsRepository.findByHotelId(reservation.getHotelId())
                    .orElseThrow(() -> new IllegalStateException("Hotel not found for ID: " + reservation.getHotelId()));
            Rooms room = roomsRepository.findByRoomId(reservation.getRoomId())
                    .orElseThrow(() -> new IllegalStateException("Room not found for ID: " + reservation.getRoomId()));
            ReservationPricing pricing = reservationPricingRepository.findByReservation(reservation)
                    .orElseThrow(() -> new IllegalStateException("Pricing not found for reservation ID: " + reservation.getReservationId()));
            ReservationPlans plan = reservationPlansRepository.findByReservation(reservation)
                    .orElseThrow(() -> new IllegalStateException("Plan not found for reservation ID: " + reservation.getReservationId()));
 
            ReservationResponseDTO responseDTO = new ReservationResponseDTO();
            responseDTO.setReservationId(reservation.getReservationId());
            responseDTO.setHotelName(hotel.getHotelName());
            responseDTO.setRoomType(room.getRoomType());
            responseDTO.setGuestFirstName(guest.getFirstName());
            responseDTO.setGuestLastName(guest.getLastName());
            responseDTO.setCheckIn(reservation.getCheckIn());
            responseDTO.setCheckOut(reservation.getCheckOut());
            responseDTO.setNoOfAdult(reservation.getNoOfAdult());
            responseDTO.setNoOfChild(reservation.getNoOfChild());
            responseDTO.setPlanName(plan.getPlanId().equals("PL1") ? "REGULAR PRICING PLAN" : "DISCOUNT FOR EARLY BOOKINGS");
            responseDTO.setStatus(reservation.getStatus());
            responseDTO.setPrice(pricing.getPrice());
            return responseDTO;
        }).collect(Collectors.toList());
    }

    private void updateRoomAvailability(ReservationRequestDTO requestDTO) {
        if ("Confirmed".equalsIgnoreCase(requestDTO.getStatus())) {
            LocalDate checkIn = requestDTO.getCheckIn().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate checkOut = requestDTO.getCheckOut().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            for (LocalDate date = checkIn; date.isBefore(checkOut); date = date.plusDays(1)) {
                Date currentDate = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());

                Hotels hotels = hotelsRepository.findByHotelName(requestDTO.getHotelName())
                        .orElseThrow(() -> {
                            logger.warn("Hotel not found: {}", requestDTO.getHotelName());
                            return new IllegalArgumentException("Hotel not found: " + requestDTO.getHotelName());
                        });

                Rooms rooms = roomsRepository.findByRoomTypeAndHotelId(requestDTO.getRoomType(), hotels.getHotelId())
                        .orElseThrow(() -> {
                            logger.warn("Room not found: {} in hotel: {}", requestDTO.getRoomType(), requestDTO.getHotelName());
                            return new IllegalArgumentException("Room type " + requestDTO.getRoomType() + " not found in hotel");
                        });

                RoomAvailability roomAvailability = roomAvailabilityRepository
                        .findByRoomIdAndDate(rooms.getRoomId(), currentDate)
                        .orElseThrow(() -> {
                            logger.warn("Room availability not found for room: {} on date: {}", rooms.getRoomId(), currentDate);
                            return new IllegalArgumentException("Room availability data missing for date: " + currentDate);
                        });

                if (roomAvailability.getAvailabilityStock() <= 0) {
                    logger.warn("No availability for room: {} on date: {}", rooms.getRoomId(), currentDate);
                    throw new IllegalStateException("No rooms available for " + requestDTO.getRoomType() + " on " + currentDate);
                }

                roomAvailability.setAvailabilityStock(roomAvailability.getAvailabilityStock() - 1);
                roomAvailability.setUsedStock(roomAvailability.getUsedStock() + 1);
                roomAvailability.setUpdateTimestamp(LocalDateTime.now());
                roomAvailability.setUpdatedBy("system");

                try {
                    logger.info("Attempting to update room availability for date: {}", currentDate);
                    roomAvailabilityRepository.save(roomAvailability);
                    logger.info("Successfully updated room availability for date: {}", currentDate);
                } catch (Exception e) {
                    logger.error("Failed to update room availability for date: {}", currentDate, e);
                    throw e; //GlobalExceptionHandler
                }
            }
        }
    }

    private void sendReservationConfirmationMessage(String guestEmail, ReservationResponseDTO responseDTO) {
        try {
            String messageBody = String.format(
                "Dear %s %s,\n\nYour reservation has been successfully confirmed!\n\n" +
                "Reservation Details:\n" +
                "Reservation ID: %d\n" +
                "Hotel: %s\n" +
                "Room Type: %s\n" +
                "Check-In: %s\n" +
                "Check-Out: %s\n" +
                "Adults: %d\n" +
                "Children: %d\n" +
                "Plan: %s\n" +
                "Total Price: %s\n" +
                "Status: %s\n\n" +
                "Thank you for choosing our service!",
                responseDTO.getGuestFirstName(),
                responseDTO.getGuestLastName(),
                responseDTO.getReservationId(),
                responseDTO.getHotelName(),
                responseDTO.getRoomType(),
                responseDTO.getCheckIn().toString(),
                responseDTO.getCheckOut().toString(),
                responseDTO.getNoOfAdult(),
                responseDTO.getNoOfChild(),
                responseDTO.getPlanName(),
                responseDTO.getPrice().toString(),
                responseDTO.getStatus()
            );

            jmsTemplate.send("booking.confirmation.queue", session -> {
                TextMessage textMessage = session.createTextMessage();
                textMessage.setStringProperty("email", guestEmail);
                textMessage.setText(messageBody);
                return textMessage;
            });
            logger.info("Sent reservation confirmation message to queue for guest: {}", guestEmail);
        } catch (Exception e) {
            logger.error("Failed to send reservation confirmation message to queue", e);
        }
    }
}