package commonmodule.service;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import commonmodule.dto.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Service
public class HotelCommonService {

    private final ChatClient chatClient;
    private final RestTemplate restTemplate;
    private static final String HOTELAPP_CHAT_URL = "http://localhost:8083/hotelapp/chat";
    
    // In-memory store for reservations state
    private static class ReservationsState {
        String email;
        String phone;
        int step; // 0: init, 1: email, 2: phone, 3: complete
    }
 
    // In-memory store for hotel search state
    private static class HotelSearchState {
        String cityName;
        String checkInDate;
        String checkOutDate;
        int step; // 0: init, 1: city, 2: checkInDate, 3: checkOutDate, 4: complete
    }
     
    private static class BookingState {
        String firstName;
        String lastName;
        String email;
        String phone;
        String hotelName;
        Integer noOfAdult;  
        Integer noOfChild; 
        String roomType;
        String cityName;
        String checkInDate;
        String checkOutDate;
        int step; // 0: init, 1: firstName, 2: lastName, 3: email, 4: phone, 5: hotelName, 6: roomType, 7: city, 8: checkInDate, 9: checkOutDate, 10: complete
    }

    private final Map<String, BookingState> bookingStates = new ConcurrentHashMap<>();
    private final Map<String, ReservationsState> reservationsStates = new ConcurrentHashMap<>();
    private final Map<String, HotelSearchState> hotelSearchStates = new ConcurrentHashMap<>();

    @Autowired
    public HotelCommonService(ChatClient.Builder chatClientBuilder, RestTemplate restTemplate) {
        this.chatClient = chatClientBuilder.build();
        this.restTemplate = restTemplate;
    }

    public ChatResponseDTO processUserInput(ChatRequestDTO request) {
        String message = request.getMessage().toLowerCase().trim();
        String userId = request.getUserId() != null ? request.getUserId() : "default_user";
        String response;
        
        reservationsStates.computeIfAbsent(userId, k -> new ReservationsState());
        hotelSearchStates.computeIfAbsent(userId, k -> new HotelSearchState());
        bookingStates.computeIfAbsent(userId, k -> new BookingState());
        
        ReservationsState resState = reservationsStates.get(userId);
        HotelSearchState searchState = hotelSearchStates.get(userId);
        BookingState bookingState = bookingStates.get(userId);

        if ((message.contains("book") || message.contains("reserve")) && bookingState.step == 0) {
            bookingState.step = 1;
            response = askForFirstName();
        } 
        // Check if user wants to view reservations
        else if ((message.contains("view") || message.contains("my reservations") || message.contains("get reservations")) && resState.step == 0) {
            resState.step = 1;
            response = askForEmail();
        }
        // Check if user wants to search hotels
        else if ((message.contains("find") || message.contains("search") || message.contains("hotels")) && searchState.step == 0) {
            searchState.step = 1;
            response = askForCity();
        }
        
        else if (bookingState.step > 0) {
            response = handleBookingStep(userId, bookingState, message, request.getGuestId());
        }
        else if (resState.step > 0) {
            response = handleReservationsStep(userId, resState, message, request.getGuestId());
        }
        else if (searchState.step > 0) {
            response = handleHotelSearchStep(userId, searchState, message);
        } 
        else {
            response = guideUser(message);
        }

        ChatResponseDTO chatResponse = new ChatResponseDTO();
        chatResponse.setResponse(response);
        return chatResponse;
    }
    
    private String askForFirstName() {
        String prompt = "You are a hotel chatbot. Ask the user for their first name in a casual way. Keep it short";
        return chatClient.prompt().user(prompt).call().content();
    }
 
    private String askForLastName() {
        String prompt = "You are a hotel chatbot. Ask the user for their last name in a casual way. Keep it short";
        return chatClient.prompt().user(prompt).call().content();
    }

    private String askForEmail() {
    String prompt = "You are a hotel chatbot. Ask the user for their email address in a casual way. Keep it short";
    return chatClient.prompt().user(prompt).call().content();
}

    private String askForPhone() {
    	String prompt = "You are a hotel chatbot. Ask the user for their phone number in a casual way. Keep it short";
    return chatClient.prompt().user(prompt).call().content();
    }

    private String askForHotelName(String registerResponse) {
        try {
            String prompt = """
                You are a hotel chatbot. The registration response was: "%s". Add the guest id in the response to the user.
                Ask the user for the hotel name to book in a casual, friendly way. Keep it short and natural.
                Example: "Great, what's the name of the hotel you'd like to book?"
                """.formatted(registerResponse);
            return chatClient.prompt().user(prompt).call().content();
        } catch (Exception e) {
            return "Sorry, something went wrong. Please provide the hotel name to continue.";
        }
    }
    
    private String askForNumberOfAdults() {
        String prompt = "You are a hotel chatbot. Ask the user for the number of adults for the booking in a casual way. Keep it short. Example: 'How many adults will be staying?'";
        return chatClient.prompt().user(prompt).call().content();
    }

    private String askForNumberOfChildren() {
        String prompt = "You are a hotel chatbot. Ask the user for the number of children for the booking in a casual way. Mention that they can say 0 if none. Keep it short. Example: 'How many children? (Say 0 if none)'";
        return chatClient.prompt().user(prompt).call().content();
    }
    
    private String askForAnotherRoom() {
        String prompt = "You are a hotel chatbot. Ask the user if they want to book another room in a casual way. Keep it short. Example: 'Would you like to book another room?'";
        return chatClient.prompt().user(prompt).call().content();
    }

    private String askForRoomType() {
    	String prompt = "You are a hotel chatbot. Ask the user for the room type to book in a casual way. Keep it short";
        return chatClient.prompt().user(prompt).call().content();
    }

    private String askForCity() {
    	  String prompt = "You are a hotel chatbot. Ask the user for the city to search hotels in a casual way. Keep it short";
          return chatClient.prompt().user(prompt).call().content();
     }

    private String askForCheckInDate() {        
    	String prompt = "You are a hotel chatbot. Ask the user for the check-in date (MM-DD-YYYY) to search hotels in a casual way. Keep it short";
    return chatClient.prompt().user(prompt).call().content();
}

    private String askForCheckOutDate() {        
    	String prompt = "You are a hotel chatbot. Ask the user for the check-out date (MM-DD-YYYY) to search hotels in a casual way. Keep it short";
    return chatClient.prompt().user(prompt).call().content();
}
    
    private String handleReservationsStep(String userId, ReservationsState state, String message, String guestId) {
        switch (state.step) {
            case 1: // Expecting email
                if (!message.matches("^[\\w.%-]+@[\\w.-]+\\.[a-z]{2,}$")) {
                    return "Please provide a valid email address (e.g., john@example.com).";
                }
                state.email = message;
                state.step = 2;
                return askForPhone();
            case 2: // Expecting phone
                if (!message.matches("^\\+?\\d{10,15}$")) {
                    return "Please provide a valid phone number with country code (e.g., +1234567890).";
                }
                state.phone = message;
                state.step = 3;
 
                // Construct view reservations command
                String viewCommand = String.format(
                    "View my reservations email %s phone %s",
                    state.email, state.phone
                );
 
                // Send to hotelapp
                ChatRequestDTO hotelRequest = new ChatRequestDTO();
                hotelRequest.setMessage(viewCommand);
                hotelRequest.setUserId(userId);
                hotelRequest.setGuestId(guestId); // Pass the guestId if available
                String hotelResponse = callHotelAppChat(hotelRequest);
 
                // Reset state
                reservationsStates.remove(userId);
 
                return hotelResponse;
            default:
                // Should not reach here
                reservationsStates.remove(userId);
                return "Reservation viewing process interrupted. Please start again by saying 'view my reservations'.";
        }
    }
    
    private String handleHotelSearchStep(String userId, HotelSearchState state, String message) {
        switch (state.step) {
            case 1: // Expecting city
                if (message.isEmpty()) {
                    return "Please provide a city name.";
                }
                state.cityName = message;
                state.step = 2;
                return askForCheckInDate();
            case 2: // Expecting check-in date
                if (!message.matches("^\\d{2}-\\d{2}-\\d{4}$")) {
                    return "Please provide a valid check-in date (MM-DD-YYYY).";
                }
                state.checkInDate = message;
                state.step = 3;
                return askForCheckOutDate();
            case 3: // Expecting check-out date
                if (!message.matches("^\\d{2}-\\d{2}-\\d{4}$")) {
                    return "Please provide a valid check-out date (MM-DD-YYYY).";
                }
                state.checkOutDate = message;
                state.step = 4;
 
                // Construct search command
                String searchCommand = String.format(
                    "Find hotels in %s from %s to %s",
                    state.cityName, state.checkInDate, state.checkOutDate
                );
 
                // Send to hotelapp
                ChatRequestDTO hotelRequest = new ChatRequestDTO();
                hotelRequest.setMessage(searchCommand);
                hotelRequest.setUserId(userId);
                String hotelResponse = callHotelAppChat(hotelRequest);
 
                // Reset state
                hotelSearchStates.remove(userId);
 
                return hotelResponse;
            default:
                // Should not reach here
                hotelSearchStates.remove(userId);
                return "Hotel search process interrupted. Please start again by saying 'search hotels'.";
        }
    }

    private String handleBookingStep(String userId, BookingState state, String message, String guestId) {
    	// Check for cancellation at any step
        if (message.contains("cancel")) {
            bookingStates.remove(userId);
            return "Booking process cancelled. You can start again by saying 'book hotel'.";
        }
        switch (state.step) {
            case 1: // Expecting first name
                if (message.isEmpty()) return "Please provide your first name.";
                state.firstName = message;
                state.step = 2;
                return askForLastName();
            case 2: // Expecting last name
                if (message.isEmpty()) return "Please provide your last name.";
                state.lastName = message;
                state.step = 3;
                return askForEmail();
            case 3: // Expecting email
                if (!message.matches("^[\\w.%-]+@[\\w.-]+\\.[a-z]{2,}$"))
                    return "Please provide a valid email address (e.g., john@example.com).";
                state.email = message;
                state.step = 4;
                return askForPhone();
            case 4: // Expecting phone
                if (!message.matches("^\\+?\\d{10,15}$"))
                    return "Please provide a valid phone number with country code (e.g., +1234567890).";
                state.phone = message;
                state.step = 5;

                // Perform registration
                String registerCommand = String.format(
                    "Register guest name %s %s email %s phone %s",
                    state.firstName, state.lastName, state.email, state.phone
                );
                ChatRequestDTO registrationRequest = new ChatRequestDTO();
                registrationRequest.setMessage(registerCommand);
                registrationRequest.setUserId(userId);
                String registerResponse = callHotelAppChat(registrationRequest);

                // Check if registration was successful
                if (registerResponse.toLowerCase().contains("successfully") || 
                		registerResponse.toLowerCase().contains("exist")) {
                    return askForHotelName(registerResponse);
                } else {
                    bookingStates.remove(userId);
                    return registerResponse + " Please try booking again by saying 'book hotel'.";
                }
            case 5: // Expecting hotel name
                if (message.isEmpty()) return "Please provide a hotel name.";
                state.hotelName = message;
                state.step = 6;
                return askForCity();
            case 6: // Expecting city
                if (message.isEmpty()) return "Please provide a city name.";
                state.cityName = message;
                state.step = 7;
                return askForRoomType();
            case 7: // Expecting room type
                if (message.isEmpty()) return "Please provide a room type.";
                state.roomType = message;
                state.step = 8;
                return askForNumberOfAdults();
            case 8: // Expecting number of adults
                try {
                    int adults = Integer.parseInt(message);
                    if (adults < 1) {
                        return "Please provide a valid number of adults (at least 1).";
                    }
                    state.noOfAdult = adults;
                    state.step = 9;
                    return askForNumberOfChildren();
                } catch (NumberFormatException e) {
                    return "Please provide a valid number of adults (e.g., 2).";
                }
            case 9: // Expecting number of children
                try {
                    int children = Integer.parseInt(message);
                    if (children < 0) {
                        return "Please provide a valid number of children (0 or more).";
                    }
                    state.noOfChild = children;
                    state.step = 10;
                    return askForCheckInDate();
               } catch (NumberFormatException e) {
                    return "Please provide a valid number of children.";
               }
            case 10: // Expecting check-in date
                if (!message.matches("^\\d{2}-\\d{2}-\\d{4}$"))
                    return "Please provide a valid check-in date (e.g., 04-10-2025).";
                state.checkInDate = message;
                state.step = 11;
                return askForCheckOutDate();
            case 11: // Expecting check-out date
                if (!message.matches("^\\d{2}-\\d{2}-\\d{4}$"))
                    return "Please provide a valid check-out date (e.g., 04-12-2025).";
                state.checkOutDate = message;
                state.step = 12;

                // Perform booking
                String bookingCommand = String.format(
                        "Book hotel %s room %s in %s from %s to %s email %s phone %s adults %d children %d",
                        state.hotelName, state.roomType, state.cityName, state.checkInDate, state.checkOutDate,
                        state.email, state.phone, state.noOfAdult, state.noOfChild
                );
                ChatRequestDTO bookingRequest = new ChatRequestDTO();
                bookingRequest.setMessage(bookingCommand);
                bookingRequest.setUserId(userId);
                bookingRequest.setGuestId(guestId);
                String hotelResponse = callHotelAppChat(bookingRequest);

               // bookingStates.remove(userId);
                return hotelResponse + "\n" + askForAnotherRoom();
            case 12: // Expecting another room decision
                if (message.contains("yes") || message.contains("yep") || message.contains("sure")) {
                    state.step = 7; // Repeat from room type
                    return askForRoomType();
                } else if (message.contains("no") || message.contains("nope") || message.contains("that's all")) {
                    bookingStates.remove(userId);
                    return "Booking completed. Anything else I can help with?";
                } else {
                    return "Please say 'yes' to book another room or 'no' to complete the booking.";
                }
            default:
                bookingStates.remove(userId);
                return "Booking process interrupted. Please start again by saying 'book hotel'.";
        }
    }

    private String callHotelAppChat(ChatRequestDTO request) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<ChatRequestDTO> httpEntity = new HttpEntity<>(request, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(HOTELAPP_CHAT_URL, httpEntity, String.class);
            return response.getBody() != null ? response.getBody() : "Error: No response from hotel service.";
        } catch (Exception e) {
            return "Error communicating with hotel service: " + e.getMessage();
        }
    }



    private String guideUser(String message) {
        String prompt = """
            You are a helpful hotel chatbot. The user said: "%s".
            Their intent is unclear or unrelated. Guide them with options like:
            - "Find hotels"
            - "Book hotel"
            - "View reservations"
            """.formatted(message);
        return chatClient.prompt().user(prompt).call().content();
    }
}
