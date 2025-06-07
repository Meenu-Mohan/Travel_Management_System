package com.ibsplc.hotelbatchmanagement.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import com.ibsplc.hotelbatchmanagement.entity.Hotel;
import com.ibsplc.hotelbatchmanagement.exception.BatchProcessingException;

@Component
public class HotelItemProcessor implements ItemProcessor<Hotel, Hotel> {
    @Override
    public Hotel process(Hotel hotel) throws Exception {
        // Validate required fields
        if (hotel.getHotelId() == null || hotel.getHotelId().trim().isEmpty()) {
            throw new BatchProcessingException("Hotel Id cannot be empty");
        }
        if (hotel.getHotelName() == null || hotel.getHotelName().trim().isEmpty()) {
            throw new BatchProcessingException("Hotel Name cannot be empty");
        }
        if (hotel.getDescription() == null || hotel.getDescription().trim().isEmpty()) {
            throw new BatchProcessingException("Description cannot be empty");
        }
        if (hotel.getAddress() == null || hotel.getAddress().trim().isEmpty()) {
            throw new BatchProcessingException("Address cannot be empty");
        }
        
 
        // Normalize data (e.g., trim and convert to uppercase)
        hotel.setHotelId(hotel.getHotelId().trim().toUpperCase());
        hotel.setHotelName(hotel.getHotelName().trim().toUpperCase());
        hotel.setDescription(hotel.getDescription().trim().toUpperCase());
        hotel.setAddress(hotel.getAddress().trim().toUpperCase());
 
        // No need to set createdBy/updatedBy; they are handled by @PrePersist/@PreUpdate
        return hotel;
    }
}
