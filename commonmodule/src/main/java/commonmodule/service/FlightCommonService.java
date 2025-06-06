package commonmodule.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import commonmodule.dto.*;

import org.springframework.beans.factory.annotation.Value;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class FlightCommonService {

	private final LlamaApiService llamaApiService;
	private final RestTemplate restTemplate;
	private final ObjectMapper objectMapper;
    private static final Logger logger = LogManager.getLogger(FlightCommonService.class);	
	private final Map<String, BookingSession> bookingSessions = new ConcurrentHashMap<>();
    private volatile String activeSessionId; // Store the most recent session ID 

    @Value("${VIEW_RESERVATION_URL}")
    private String viewTicketUrl;
    
    
    @Value("${BOOK_TICKET_URL}")
    private String bookTicketUrl;
    
    
    @Value("${FLIGHT_SEARCH_URL}")
    private String flightSearchUrl; 
    
    
	public FlightCommonService(LlamaApiService llamaApiService, RestTemplate restTemplate) {
		this.llamaApiService = llamaApiService;
		this.restTemplate = restTemplate;
		this.objectMapper = new ObjectMapper();
	}

	public String processUserInput(String userInput) {
		String llamaResponse = llamaApiService.processInput(userInput);
		logger.info("llama response: {}",llamaResponse);
		try {
			llamaResponse = llamaResponse.replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", "").replaceAll("\n", "\\\\n")
					.replaceAll("\r", "\\\\r");

			LlamaResponse parsedResponse = objectMapper.readValue(llamaResponse, LlamaResponse.class);
			String action = parsedResponse.getAction();
			String intent = parsedResponse.getIntent();
			logger.info("action: {}, intent: {}",action,intent);
			if (action == null) {
				return "Sorry, no action specified in the response.";
			}

			if (action.equalsIgnoreCase("flight")) {
				switch (intent) {
				case "check_availability":
					return checkFlightAvailability(parsedResponse);
				case "book_flight":
					return initiateBooking(parsedResponse);
				case "passenger_details":
					return collectPassengerDetails(parsedResponse);
				case "view_ticket":
					return viewTicket(parsedResponse);
				case "error":
					return parsedResponse.getResponse();
				default:
					return parsedResponse.getResponse() != null ? parsedResponse.getResponse()
							: "I’m not sure how to help with that.";
				}
			} else if (action.equalsIgnoreCase("chat")) {
				return userChat(parsedResponse);
			} else {
				return "Unsupported action: " + action;
			}
		} catch (Exception e) {
			return "Sorry, I couldn't process your request. Error: " + e.getMessage();
		}
	}

	private String userChat(LlamaResponse parsedResponse) {
		return parsedResponse.getEntities().getOrDefault("response", "Sorry I couldn't understand");
	}

	private String viewTicket(LlamaResponse response) {
		int reservationId;
		try {
			reservationId = Integer.parseInt(response.getEntities().getOrDefault("reservationId", "0"));
			if (reservationId <= 0) {
				return "Invalid reservation ID.";
			}
		} catch (NumberFormatException e) {
			return "Invalid reservation ID format.";
		}

		String url = viewTicketUrl + reservationId;
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(List.of(MediaType.APPLICATION_JSON));
			HttpEntity<Void> entity = new HttpEntity<>(headers);
			ResponseEntity<BookingDetailsResponseDTO> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null,
					new ParameterizedTypeReference<BookingDetailsResponseDTO>() {
					});

			if (!responseEntity.getStatusCode().is2xxSuccessful()) {
				return "Failed to fetch booking details: HTTP " + responseEntity.getStatusCode();
			}

			BookingDetailsResponseDTO bookingDetails = responseEntity.getBody();
			
			if (bookingDetails != null) {
				ReservationDetailsDTO reservationDetails = bookingDetails.getReservation();
				StringBuilder result = new StringBuilder("Booking Details:\n");
				result.append(reservationDetails.toString()).append("\n");
				List<PassengerDetailsDTO> passengerDetailsList = bookingDetails.getPassengers();
				int index = 1;
				for (PassengerDetailsDTO passengerDetails : passengerDetailsList) {
					result.append(index++).append(". ").append(passengerDetails.toString()).append("\n");
				}
				return result.toString();
			} else {
				return "No booking details found.";
			}
		} catch (Exception e) {
			return "Error occurred while fetching booking details: " + e.getMessage();
		}
	}

	private String initiateBooking(LlamaResponse response) {
		String sessionId = generateSessionId(); // Unique session ID for this booking
		String flightId = response.getEntities().getOrDefault("flightId", "");
		int numberOfTickets;
		try {
			numberOfTickets = Integer.parseInt(response.getEntities().getOrDefault("numberOfTickets", "1"));
		} catch (NumberFormatException e) {
			return "Invalid number of tickets.";
		}
		String reservedClass = response.getEntities().getOrDefault("reservedClass", "");

		if (flightId.isEmpty() || numberOfTickets <= 0 || reservedClass.isEmpty()) {
			return "Please provide valid flight ID, number of tickets, and reserved class in the format: "
					+ "Book a ticket for 'no_of_tickets' passengers in 'reserved_class_name' class for flightid 'flight_id'";
		}

		// Store booking session
		BookingSession session = new BookingSession(flightId, numberOfTickets, reservedClass);
		bookingSessions.put(sessionId, session);
        activeSessionId = sessionId; // Set the active session ID

		return "Booking initiated. Please provide passenger details one by one for " + numberOfTickets
				+ " passengers in the format:'Passenger details is passengerName:xxx, age:xx, phoneNumber:xxx, emailAddress:xxx, dateOfBirth:YYYY-MM-DD, passportNumber:xxx'";
	}

	private String collectPassengerDetails(LlamaResponse response) {
        String sessionId = activeSessionId;
		BookingSession session = bookingSessions.get(sessionId);

		if (session == null) {
			return "No active booking session found. Please initiate a booking first.";
		}

		try {
			
			PassengerDetailsDTO passengerDetail = new PassengerDetailsDTO();
			String passengerName= response.getEntities().getOrDefault("passengerName", "");
			if (passengerName.isEmpty()) {
				return "Passenger name is required.";
			}
			passengerDetail.setPassengerName(passengerName);


			String ageStr = response.getEntities().getOrDefault("age", "");
            if (ageStr.isEmpty()) {
                return "Age is required.";
            }
            int age = Integer.parseInt(ageStr);
            if (age <= 0) {
                return "Invalid age provided.";
            }
            passengerDetail.setAge(age);

            String phoneStr = response.getEntities().getOrDefault("phoneNumber", "");
            if (!phoneStr.matches("\\d{10}") || phoneStr.isEmpty()) {
                return "Invalid phone number format. Use 10 digits.";
            }
            passengerDetail.setPhoneNumber(Long.parseLong(phoneStr));
			
			String email=response.getEntities().getOrDefault("emailAddress", "");
			if (email.isEmpty() || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
				return "Invalid Email address provided";
			}
            passengerDetail.setEmailAddress(email);
            
			String dobStr = response.getEntities().getOrDefault("dateOfBirth", "");
			if (dobStr.isEmpty()) {
				return "Date of Birth is required.";
			}
			passengerDetail.setDateOfBirth(LocalDate.parse(dobStr, DateTimeFormatter.ISO_LOCAL_DATE).atStartOfDay());

			String passport = response.getEntities().getOrDefault("passportNumber", "");
            if (passport.isEmpty()) {
                return "Passport number is required.";
            }
            passengerDetail.setPassportNumber(passport);

			session.addPassenger(passengerDetail);

			if (session.getPassengers().size() < session.getNumberOfTickets()) {
				return "Passenger details recorded. Please provide details for passenger "
						+ (session.getPassengers().size() + 1) + " of " + session.getNumberOfTickets()+" ticket";
			} else {
				// All passengers collected, proceed to booking
				return completeBooking(sessionId, session);
			}
		} 
		 catch (DateTimeParseException e) {
			    return "Invalid Date of Birth format. Use YYYY-MM-DD.";
			}catch (Exception e) {
			return "Error processing passenger details: " + e.getMessage();
		}
	}

	private String completeBooking(String sessionId, BookingSession session) {
		String url = UriComponentsBuilder.fromHttpUrl(bookTicketUrl)
				.queryParam("flightId", session.getFlightId())
				.queryParam("numberOfTickets", session.getNumberOfTickets())
				.queryParam("reservedClass", session.getReservedClass()).toUriString();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<List<PassengerDetailsDTO>> requestEntity = new HttpEntity<>(session.getPassengers(), headers);

		try {
			ResponseEntity<ReservationDetailsDTO> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity,
					ReservationDetailsDTO.class);

			if (response.getStatusCode().is2xxSuccessful()) {
				// Clean up session
				bookingSessions.remove(sessionId);
				activeSessionId = null;
				return "Successfully booked your flight. Reservation ID: "
						+ (response.getBody() != null ? response.getBody().getReservationId() : "unknown");
			} else {
				return "Failed to book flight: HTTP " + response.getStatusCode();
			}
		} catch (Exception e) {
			return "Error occurred while booking flight: " + e.getMessage();
		}
	}

	private String checkFlightAvailability(LlamaResponse response) {
		String depAirport = response.getEntities().getOrDefault("depAirport", "");
		String arrAirport = response.getEntities().getOrDefault("arrAirport", "");
		String departureDateStr = response.getEntities().getOrDefault("departureDate", LocalDate.now().toString());
		String arrivalDateStr = response.getEntities().getOrDefault("arrivalDate", LocalDate.now().toString());
		int numberOfTickets;
		try {
			numberOfTickets = Integer.parseInt(response.getEntities().getOrDefault("noOFtickets", "1"));
		} catch (NumberFormatException e) {
			return "Invalid number of tickets.";
		}

		try {
			LocalDate departureDate = LocalDate.parse(departureDateStr, DateTimeFormatter.ISO_LOCAL_DATE);
			LocalDate arrivalDate = LocalDate.parse(arrivalDateStr, DateTimeFormatter.ISO_LOCAL_DATE);

			String url = UriComponentsBuilder.fromHttpUrl(flightSearchUrl)
					.queryParam("numberOfTickets", numberOfTickets).queryParam("depAirport", depAirport)
					.queryParam("arrAirport", arrAirport).queryParam("departureDate", departureDate)
					.queryParam("arrivalDate", arrivalDate).toUriString();

			ResponseEntity<List<FlightDTO>> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null,
					new ParameterizedTypeReference<List<FlightDTO>>() {
					});

			if (!responseEntity.getStatusCode().is2xxSuccessful()) {
				return "Failed to fetch flights: HTTP " + responseEntity.getStatusCode();
			}

			List<FlightDTO> flights = responseEntity.getBody();
			if (flights != null) {
				StringBuilder result = new StringBuilder("Available flights: ");
				int index = 1;
				for (FlightDTO flight : flights) {
					result.append(index++).append(". ").append(flight.toString()).append("\n");
				}
				return result.toString();
			} else {
				return "No flights found.";
			}
		} catch (Exception e) {
            return "Error occurred while fetching flights: " + e.getMessage();
		}
	}

	// Helper class to manage booking session
	private static class BookingSession {
		private final String flightId;
		private final int numberOfTickets;
		private final String reservedClass;
		private final List<PassengerDetailsDTO> passengers;

		public BookingSession(String flightId, int numberOfTickets, String reservedClass) {
			this.flightId = flightId;
			this.numberOfTickets = numberOfTickets;
			this.reservedClass = reservedClass;
			this.passengers = new ArrayList<>();
		}

		public String getFlightId() {
			return flightId;
		}

		public int getNumberOfTickets() {
			return numberOfTickets;
		}

		public String getReservedClass() {
			return reservedClass;
		}

		public List<PassengerDetailsDTO> getPassengers() {
			return passengers;
		}

		public void addPassenger(PassengerDetailsDTO passenger) {
			passengers.add(passenger);
		}
	}

	// Generate a unique session ID (replace with UUID in production)
	private String generateSessionId() {
		return String.valueOf(System.currentTimeMillis() + (int) (Math.random() * 1000));
	}
}