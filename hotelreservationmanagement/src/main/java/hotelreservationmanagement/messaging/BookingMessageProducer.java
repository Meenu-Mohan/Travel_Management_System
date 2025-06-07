package hotelreservationmanagement.messaging;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import hotelreservationmanagement.dto.ReservationResponseDTO;

@Log4j2
@Component
public class BookingMessageProducer {

    @Autowired
    private JmsTemplate jmsTemplate;

    private static final String BOOKING_QUEUE = "booking.confirmation.queue";

    public void sendBookingConfirmation(ReservationResponseDTO reservation) {
        try {
            //log.info("Sending booking confirmation to queue for reservation: {}", reservation.getReservationId());
            jmsTemplate.convertAndSend(BOOKING_QUEUE, reservation);
        } catch (Exception e) {
            //log.error("Error sending booking confirmation to queue", e);
            throw new RuntimeException("Failed to send booking confirmation", e);
        }
    }
}
