package com.ibsplc.flightreservation.controller;

import com.ibsplc.flightreservation.entity.PassengerDetails;
import com.ibsplc.flightreservation.entity.ReservationDetails;
import java.util.List;

public class BookingDetailsResponse {
    private ReservationDetails reservation;
    private List<PassengerDetails> passengers;

    public BookingDetailsResponse(ReservationDetails reservation, List<PassengerDetails> passengers) {
        this.reservation = reservation;
        this.passengers = passengers;
    }

    public ReservationDetails getReservation() {
        return reservation;
    }

    public void setReservation(ReservationDetails reservation) {
        this.reservation = reservation;
    }

    public List<PassengerDetails> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<PassengerDetails> passengers) {
        this.passengers = passengers;
    }
}