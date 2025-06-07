package com.ibsplc.hotelbatchmanagement.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import com.ibsplc.hotelbatchmanagement.entity.Rooms;
import com.ibsplc.hotelbatchmanagement.exception.BatchProcessingException;

@Component
public class RoomsItemProcessor implements ItemProcessor<Rooms, Rooms> {
    @Override
    public Rooms process(Rooms rooms) throws Exception {
        // Validate required fields
        if (rooms.getRoomType() == null || rooms.getRoomType().trim().isEmpty()) {
            throw new BatchProcessingException("Room Type cannot be empty");
        }
        if (rooms.getMinCapacity() == null) {
            throw new BatchProcessingException("Min Capacity cannot be empty");
        }
        if (rooms.getMaxCapacity() == null) {
            throw new BatchProcessingException("Max Capacity cannot be empty");
        }
        if (rooms.getAmenities() == null || rooms.getAmenities().trim().isEmpty()) {
            throw new BatchProcessingException("Amenities cannot be empty");
        }

        // Normalize data (e.g., trim and convert to uppercase)
        rooms.setRoomType(rooms.getRoomType().trim().toUpperCase());
        rooms.setAmenities(rooms.getAmenities().trim().toUpperCase());

        // No need to set createdBy/updatedBy; they are handled by @PrePersist/@PreUpdate
        return rooms;
    }
}
