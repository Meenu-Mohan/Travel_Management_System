package hotelavailability.dto;

import java.util.List;

import hotelavailability.entity.Hotels;
import hotelavailability.entity.Rooms;

public class HotelAndRoomsDTO {
    private Hotels hotel;
    private List<Rooms> availableRooms;

    // Constructor
    public HotelAndRoomsDTO(Hotels hotel, List<Rooms> availableRooms) {
        this.hotel = hotel;
        this.availableRooms = availableRooms;
    }

    // Getters and Setters
    public Hotels getHotel() {
        return hotel;
    }

    public void setHotel(Hotels hotel) {
        this.hotel = hotel;
    }

    public List<Rooms> getAvailableRooms() {
        return availableRooms;
    }

    public void setAvailableRooms(List<Rooms> availableRooms) {
        this.availableRooms = availableRooms;
    }
}
