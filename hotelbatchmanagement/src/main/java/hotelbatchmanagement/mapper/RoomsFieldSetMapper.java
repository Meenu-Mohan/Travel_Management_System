package hotelbatchmanagement.mapper;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import hotelbatchmanagement.entity.Rooms;

public class RoomsFieldSetMapper implements FieldSetMapper<Rooms> {
    @Override
    public Rooms mapFieldSet(FieldSet fieldSet) throws BindException {
    	Rooms rooms = new Rooms();
        // Map the other fields
    	rooms.setRoomId(fieldSet.readString("room_id"));
        rooms.setHotelId(fieldSet.readString("hotel_id")); // CSV uses "name", entity uses "city_name"
        rooms.setRoomType(fieldSet.readString("room_type"));
        rooms.setMinCapacity(fieldSet.readInt("min_capacity"));
        rooms.setMaxCapacity(fieldSet.readInt("max_capacity"));
        rooms.setAmenities(fieldSet.readString("amenities"));
        // Note: We don't map createTimestamp, updateTimestamp, createdBy, updatedBy
        // because they are set by @PrePersist and @PreUpdate in the City entity
        return rooms;
    }
}