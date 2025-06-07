package com.ibsplc.hotelbatchmanagement.processor;


import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import com.ibsplc.hotelbatchmanagement.entity.RoomAvailability;
import com.ibsplc.hotelbatchmanagement.exception.BatchProcessingException;


@Component
public class RoomAvailabilityProcessor implements ItemProcessor<RoomAvailability, RoomAvailability> {

    @Override
    public RoomAvailability process(RoomAvailability roomAvailability) throws Exception {
        // Validate required fields
        if (roomAvailability.getAvailabilityId() == null || roomAvailability.getAvailabilityId().trim().isEmpty()) {
            throw new BatchProcessingException("Availability ID cannot be empty");
        }
        if (roomAvailability.getRoomId() == null || roomAvailability.getRoomId().trim().isEmpty()) {
            throw new BatchProcessingException("Room ID cannot be empty");
        }
        if (roomAvailability.getDate() == null) {
            throw new BatchProcessingException("Date cannot be null");
        }
        if (roomAvailability.getAvailabilityStock() == null) {
            throw new BatchProcessingException("Availability stock cannot be null");
        }
        if (roomAvailability.getUsedStock() == null) {
            throw new BatchProcessingException("Used stock cannot be null");
        }
        if (roomAvailability.getTotalStock() == null) {
            throw new BatchProcessingException("Total stock cannot be null");
        }

        // Additional validation: Ensure total_stock >= used_stock + availability_stock
        if (roomAvailability.getTotalStock() < (roomAvailability.getUsedStock() + roomAvailability.getAvailabilityStock())) {
            throw new BatchProcessingException("Total stock must be greater than or equal to used stock plus availability stock");
        }

        // Normalize data (e.g., trim IDs)
        roomAvailability.setAvailabilityId(roomAvailability.getAvailabilityId().trim());
        roomAvailability.setRoomId(roomAvailability.getRoomId().trim());

      
        return roomAvailability;
    }
}