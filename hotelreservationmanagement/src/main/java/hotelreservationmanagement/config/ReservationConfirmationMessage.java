package hotelreservationmanagement.config;

import java.io.Serializable;

public class ReservationConfirmationMessage implements Serializable {
    private static final long serialVersionUID = 1L; // Recommended for Serializable classes
    private String email;
    private String messageBody;

    public ReservationConfirmationMessage(String email, String messageBody) {
        this.email = email;
        this.messageBody = messageBody;
    }

    public String getEmail() {
        return email;
    }

    public String getMessageBody() {
        return messageBody;
    }
}