// ReservationDetailsDTO.java
package commonmodule.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReservationDetailsDTO {
	private Integer reservationId;
	private String flightId;
	private LocalDateTime bookingDate;
	private String reservationStatus;
	private String reservedClass;
	private String seatNumber;

	public Integer getReservationId() {
		return reservationId;
	}

	public void setReservationId(Integer reservationId) {
		this.reservationId = reservationId;
	}

	public String getFlightId() {
		return flightId;
	}

	public void setFlightId(String flightId) {
		this.flightId = flightId;
	}

	public LocalDateTime getBookingDate() {
		return bookingDate;
	}

	public void setBookingDate(LocalDateTime bookingDate) {
		this.bookingDate = bookingDate;
	}

	public String getReservationStatus() {
		return reservationStatus;
	}

	public void setReservationStatus(String reservationStatus) {
		this.reservationStatus = reservationStatus;
	}

	public String getReservedClass() {
		return reservedClass;
	}

	public void setReservedClass(String reservedClass) {
		this.reservedClass = reservedClass;
	}

	public String getSeatNumber() {
		return seatNumber;
	}

	public void setSeatNumber(String seatNumber) {
		this.seatNumber = seatNumber;
	}

	@Override
    public String toString() {
        return "Reservation Details: " +
               " Reservation ID=" + reservationId +
               ", Flight ID=" + flightId +
               ", Booking Date=" + bookingDate +
               ", Status=" + reservationStatus +
               ", Class=" + reservedClass +
               ", Seat Number=" + seatNumber;
    }
}