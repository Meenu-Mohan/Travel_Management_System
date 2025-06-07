package com.ibsplc.hotelbatchmanagement.mapper;

import java.time.LocalDateTime;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;
import com.ibsplc.hotelbatchmanagement.entity.RoomAvailability;


public class RoomAvailabilityFieldSetMapper implements FieldSetMapper<RoomAvailability> {
    @Override
    public RoomAvailability mapFieldSet(FieldSet fieldSet) throws BindException {
        RoomAvailability roomAvailability = new RoomAvailability();
        
        roomAvailability.setAvailabilityId(fieldSet.readString("availability_id"));
        roomAvailability.setRoomId(fieldSet.readString("room_id"));
        roomAvailability.setDate(fieldSet.readDate("date", "MM-dd-yyyy"));
        roomAvailability.setAvailabilityStock(fieldSet.readInt("available_stock"));
        roomAvailability.setUsedStock(fieldSet.readInt("used_stock"));
        roomAvailability.setTotalStock(fieldSet.readInt("total_stock"));
        
     // Explicitly set required fields to avoid null values
        roomAvailability.setCreatedBy("SYSTEM"); // Matches @PrePersist logic
        roomAvailability.setUpdatedBy("SYSTEM"); // Matches @PreUpdate logic
        roomAvailability.setCreateTimestamp(LocalDateTime.now()); // Ensure non-null
        roomAvailability.setUpdateTimestamp(LocalDateTime.now()); // Ensure non-null
        
        return roomAvailability;
    }
}
