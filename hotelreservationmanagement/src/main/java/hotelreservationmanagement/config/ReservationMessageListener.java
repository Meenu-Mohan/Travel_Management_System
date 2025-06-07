package hotelreservationmanagement.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import jakarta.jms.Message; 
import jakarta.jms.TextMessage;

@Component
public class ReservationMessageListener {

    @Autowired
    private JavaMailSender mailSender;
    
    private static final Logger logger = LogManager.getLogger(ReservationMessageListener.class);

    @JmsListener(destination = "booking.confirmation.queue")
    public void onMessageReceived(Message message) {
        try {
            if (message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message;
                String guestEmail = textMessage.getStringProperty("email");
                String messageBody = textMessage.getText();

                SimpleMailMessage email = new SimpleMailMessage();
                email.setTo(guestEmail);
                email.setSubject("Reservation Confirmation");
                email.setText(messageBody);
                mailSender.send(email);
                logger.info("Email sent to: {}", guestEmail);
            } else {
                logger.warn("Received non-TextMessage: {}", message.getClass().getName());
            }
        } catch (Exception e) {
            logger.error("Failed to send email: {}", e.getMessage());
        }
    }
}