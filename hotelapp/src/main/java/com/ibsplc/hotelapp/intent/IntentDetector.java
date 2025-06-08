package com.ibsplc.hotelapp.intent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.text.ParseException;
import com.ibsplc.hotelapp.dto.GuestRequestDTO;
import com.ibsplc.hotelapp.dto.ReservationRequestDTO;

public class IntentDetector {
    //private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM-dd-yyyy");
    private static final Pattern DATE_PATTERN = Pattern.compile("\\b(\\d{2}-\\d{2}-\\d{4})\\b");

    public static class Intent {
    	public String action; // e.g., "search_hotels", "book_hotel", "register_guest"
    	public String cityName;
    	public Date checkInDate;
    	public Date checkOutDate;
    	public String hotelName;
    	public String roomType;
    	public String email;
    	public String phone;
    	public String firstName;
    	public String lastName;
    	public Integer noOfAdult;
    	public Integer noOfChild;

        Intent(String action) {
            this.action = action;
        }

        public ReservationRequestDTO toReservationRequestDTO() {
            if (hotelName == null || roomType == null || cityName == null || checkInDate == null ||
                checkOutDate == null || email == null || phone == null) {
                return null;
            }
            ReservationRequestDTO dto = new ReservationRequestDTO();
            dto.setHotelName(hotelName);
            dto.setRoomType(roomType);
            dto.setCityName(cityName);
            dto.setCheckIn(checkInDate);
            dto.setCheckOut(checkOutDate);
            dto.setEmail(email);
            dto.setPhone(phone);
            dto.setNoOfAdult(noOfAdult != null ? noOfAdult : 1);
            dto.setNoOfChild(noOfChild != null ? noOfChild : 0);
            return dto;
        }

        public GuestRequestDTO toGuestRequestDTO() {
            if (firstName == null || lastName == null || email == null || phone == null) {
                return null;
            }
            GuestRequestDTO dto = new GuestRequestDTO();
            dto.setFirstName(firstName);
            dto.setLastName(lastName);
            dto.setEmail(email);
            dto.setPhone(phone);
            return dto;
        }
    }

    public static Intent detectIntent(String message) {
        String lowerMessage = message.toLowerCase();

        // Search hotels intent
        if (lowerMessage.contains("find") || lowerMessage.contains("search") || lowerMessage.contains("hotels")) {
            Intent intent = new Intent("search_hotels");
            extractCityAndDates(lowerMessage, intent);
            return intent;
        }

        // Book hotel intent
        if (lowerMessage.contains("book") || lowerMessage.contains("reserve")) {
            Intent intent = new Intent("book_hotel");
            extractBookingDetails(lowerMessage, intent);
            return intent;
        }

        // Register guest intent
        if (lowerMessage.contains("register") || lowerMessage.contains("guest")) {
            Intent intent = new Intent("register_guest");
            extractGuestDetails(lowerMessage, intent);
            return intent;
        }
        
      // View reservations intent
        if (lowerMessage.contains("view") || lowerMessage.contains("my reservations") || lowerMessage.contains("get reservations")) {
            Intent intent = new Intent("reservations_by_guest");
            extractContactDetails(lowerMessage, intent);
            return intent;
        }

        // Unclear intent
        return new Intent("unknown");
    }

    private static void extractCityAndDates(String message, Intent intent) {
        Pattern cityPattern = Pattern.compile("\\bin\\s+([a-zA-Z\\s]+?)(?:\\s+(?:from|to|$))");
        Matcher cityMatcher = cityPattern.matcher(message);
        if (cityMatcher.find()) {
            intent.cityName = cityMatcher.group(1).trim();
        }

        Matcher dateMatcher = DATE_PATTERN.matcher(message);
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
            Date checkIn = null;
            Date checkOut = null;

            if (dateMatcher.find()) {
                checkIn = formatter.parse(dateMatcher.group(1));
            }
            if (dateMatcher.find()) {
                checkOut = formatter.parse(dateMatcher.group(1));
            }

            intent.checkInDate = checkIn;
            intent.checkOutDate = checkOut;
        } catch (ParseException e) {
            // Invalid date format; leave as null
            System.err.println("Failed to parse dates from message: " + e.getMessage());
        }
    }


    private static void extractBookingDetails(String message, Intent intent) {
        extractCityAndDates(message, intent);
        
      // Extract hotel name (capture until 'room', 'in', 'from', or end, but stop before 'room')
        Pattern hotelPattern = Pattern.compile("\\bhotel\\s+([a-zA-Z\\s]+?)(?=\\s+(?:room|in|from|$))");
        Matcher hotelMatcher = hotelPattern.matcher(message.toLowerCase());
        if (hotelMatcher.find()) {
            String hotelName = hotelMatcher.group(1).trim();
            intent.hotelName = hotelName;
        }

        Pattern roomPattern = Pattern.compile("\\broom\\s+(\\w+)");
        Matcher roomMatcher = roomPattern.matcher(message);
        if (roomMatcher.find()) intent.roomType = roomMatcher.group(1);

        // Extract number of adults
        Pattern adultPattern = Pattern.compile("\\badults?\\s+(\\d+)");
        Matcher adultMatcher = adultPattern.matcher(message);
        intent.noOfAdult = adultMatcher.find() ? Integer.parseInt(adultMatcher.group(1)) : 1;

        // Extract number of children
        Pattern childPattern = Pattern.compile("\\bchildren\\s+(\\d+)");
        Matcher childMatcher = childPattern.matcher(message);
        intent.noOfChild = childMatcher.find() ? Integer.parseInt(childMatcher.group(1)) : 0;
        
        extractContactDetails(message, intent);
    }

    private static void extractGuestDetails(String message, Intent intent) {
        Pattern namePattern = Pattern.compile("\\bname\\s+(\\w+)\\s+(\\w+)");
        Matcher nameMatcher = namePattern.matcher(message);
        if (nameMatcher.find()) {
            intent.firstName = nameMatcher.group(1);
            intent.lastName = nameMatcher.group(2);
        }
        extractContactDetails(message, intent);
    }

    private static void extractContactDetails(String message, Intent intent) {
        Pattern emailPattern = Pattern.compile("\\b[\\w.%-]+@[\\w.-]+\\.[a-z]{2,}\\b");
        Matcher emailMatcher = emailPattern.matcher(message);
        if (emailMatcher.find()) intent.email = emailMatcher.group();

        Pattern phonePattern = Pattern.compile("\\b\\+?\\d{10,15}\\b");
        Matcher phoneMatcher = phonePattern.matcher(message);
        if (phoneMatcher.find()) intent.phone = phoneMatcher.group();
    }

    private static Integer extractNumber(String message, String keyword) {
        Pattern numberPattern = Pattern.compile("\\b(\\d+)\\s+" + keyword);
        Matcher matcher = numberPattern.matcher(message);
        return matcher.find() ? Integer.parseInt(matcher.group(1)) : 1;
    }
}