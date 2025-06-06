package commonmodule.service;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IntentDetector {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM-dd-yyyy");
    private static final Pattern DATE_PATTERN = Pattern.compile("\\b(\\d{2}-\\d{2}-\\d{4})\\b");

    public static class Intent {
        String action; // e.g., "search_hotels", "book_hotel", "register_guest"
        String cityName;
        LocalDate checkInDate;
        LocalDate checkOutDate;
        String hotelName;
        String roomType;
        String email;
        String phone;
        String firstName;
        String lastName;
        Integer noOfAdult;
        Integer noOfChild;

        Intent(String action) {
            this.action = action;
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

        // Unclear intent
        return new Intent("unknown");
    }

    private static void extractCityAndDates(String message, Intent intent) {
        // Extract city (e.g., "in New York")
        Pattern cityPattern = Pattern.compile("\\bin\\s+([a-zA-Z\\s]+)(?:\\s+from|\\s+for|$)");
        Matcher cityMatcher = cityPattern.matcher(message);
        if (cityMatcher.find()) {
            intent.cityName = cityMatcher.group(1).trim();
        }

        // Extract dates
        Matcher dateMatcher = DATE_PATTERN.matcher(message);
        if (dateMatcher.find()) {
            try {
                intent.checkInDate = LocalDate.parse(dateMatcher.group(1), DATE_FORMATTER);
                if (dateMatcher.find()) {
                    intent.checkOutDate = LocalDate.parse(dateMatcher.group(1), DATE_FORMATTER);
                }
            } catch (DateTimeParseException e) {
                // Ignore invalid dates
            }
        }
    }

    private static void extractBookingDetails(String message, Intent intent) {
        extractCityAndDates(message, intent);
        Pattern hotelPattern = Pattern.compile("\\bhotel\\s+(\\w+)");
        Matcher hotelMatcher = hotelPattern.matcher(message);
        if (hotelMatcher.find()) intent.hotelName = hotelMatcher.group(1);

        Pattern roomPattern = Pattern.compile("\\broom\\s+(\\w+)");
        Matcher roomMatcher = roomPattern.matcher(message);
        if (roomMatcher.find()) intent.roomType = roomMatcher.group(1);

        intent.noOfAdult = message.contains("adult") ? extractNumber(message, "adult") : 1;
        intent.noOfChild = message.contains("child") ? extractNumber(message, "child") : 0;
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