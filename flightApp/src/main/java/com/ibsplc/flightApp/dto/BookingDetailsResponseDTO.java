// BookingDetailsResponseDTO.java
package com.ibsplc.flightApp.dto;

import lombok.Data;

import java.util.List;

@Data
public class BookingDetailsResponseDTO {
    private ReservationDetailsDTO reservation;
    private List<PassengerDetailsDTO> passengers;

    public BookingDetailsResponseDTO(ReservationDetailsDTO reservation, List<PassengerDetailsDTO> passengers) {
        this.reservation = reservation;
        this.passengers = passengers;
    }

	public ReservationDetailsDTO getReservation() {
		return reservation;
	}

	public void setReservation(ReservationDetailsDTO reservation) {
		this.reservation = reservation;
	}

	public List<PassengerDetailsDTO> getPassengers() {
		return passengers;
	}

	public void setPassengers(List<PassengerDetailsDTO> passengers) {
		this.passengers = passengers;
	}
    
    
}