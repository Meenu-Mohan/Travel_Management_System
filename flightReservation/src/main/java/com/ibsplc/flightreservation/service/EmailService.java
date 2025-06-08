// EmailService.java
package com.ibsplc.flightreservation.service;

import com.ibsplc.flightreservation.entity.ReservationDetails;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger logger = LogManager.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender mailSender;

    public void sendBookingConfirmationEmail(ReservationDetails reservation, String passengerEmail) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(passengerEmail);
            message.setSubject("Booking Confirmation - Reservation ID: " + reservation.getReservationId());
            message.setText("Dear Passenger,\n\n" +
                    "Your booking has been confirmed!\n" +
                    "Reservation ID: " + reservation.getReservationId() + "\n" +
                    "Flight ID: " + reservation.getFlightId() + "\n" +
                    "Reserved Class: " + reservation.getReservedClass() + "\n" +
                    "Seats: " + reservation.getSeatNumber() + "\n" +
                    "Booking Date: " + reservation.getBookingDate() + "\n\n" +
                    "Thank you for choosing our airline!\n" +
                    "Best regards,\nFlight Reservation Team");
            mailSender.send(message);
            logger.info("Sent email confirmation to {} for reservationId: {}", passengerEmail, reservation.getReservationId());
        } catch (Exception e) {
            logger.error("Failed to send email to {} for reservationId: {}: {}", 
                    passengerEmail, reservation.getReservationId(), e.getMessage(), e);
        }
    }
}