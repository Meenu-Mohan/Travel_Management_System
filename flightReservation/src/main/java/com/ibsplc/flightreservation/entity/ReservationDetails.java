// ReservationDetails.java
package com.ibsplc.flightreservation.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reservation_details", schema = "reservation")
public class ReservationDetails  {
    

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Integer reservationId;
    
    @Column(name = "flight_id")
    private String flightId;
    
    @Column(name = "booking_date")
    private LocalDateTime bookingDate;
    
    @Column(name = "reservation_status")
    private String reservationStatus;
    
    @Column(name = "reserved_class")
    private String reservedClass;
    
    @Column(name = "seat_number")
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
    
    
}