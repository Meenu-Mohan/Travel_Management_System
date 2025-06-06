package commonmodule.dto;

import jakarta.validation.constraints.NotBlank;


public class ChatRequestDTO {
    @NotBlank(message = "Message is required")
    private String message;
    private String guestId;
    private String userId; 

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}