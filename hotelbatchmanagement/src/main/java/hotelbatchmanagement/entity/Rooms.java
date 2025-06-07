package hotelbatchmanagement.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "Rooms", schema = "inventory")
public class Rooms {

	@Id
    @Column(name = "room_id")
    private String roomId;

    @Column(name = "hotel_id")
    private String hotelId;  

    @Column(name = "room_type", length = 50)
    private String roomType;

    @Column(name = "min_capacity")
    private Integer minCapacity;
    
    @Column(name="max_capacity")
    private Integer maxCapacity;

    @Column(name = "amenities")
    private String amenities;

    @Column(name = "create_timestamp", nullable = false, updatable = false)
    private LocalDateTime createTimestamp;

    @Column(name = "update_timestamp", nullable = false)
    private LocalDateTime updateTimestamp;

    @Column(name = "created_by", length = 50, nullable = false)
    private String createdBy;

    @Column(name = "modified_by", length = 50, nullable = false)
    private String modifiedBy;

    @PrePersist
    protected void onCreate() {
        createTimestamp = LocalDateTime.now();
        updateTimestamp = createTimestamp;
        createdBy = "SYSTEM"; 
        modifiedBy = "SYSTEM"; 
    }

    @PreUpdate
    protected void onUpdate() {
        updateTimestamp = LocalDateTime.now();
        modifiedBy = "SYSTEM"; 
    }

    // Constructors
    public Rooms() {}

    public Rooms(String roomId, String hotelId, @NotNull @Size(max = 50) String roomType, @NotNull Integer minCapacity,
    		    Integer maxCapacity,String amenities, LocalDateTime createTimestamp, LocalDateTime updateTimestamp,
                String createdBy, String modifiedBy) {
        this.roomId = roomId;
        this.hotelId = hotelId;
        this.roomType = roomType;
        this.minCapacity = minCapacity;
        this.maxCapacity = maxCapacity;
        this.amenities = amenities;
        this.createTimestamp = createTimestamp;
        this.updateTimestamp = updateTimestamp;
        this.createdBy = createdBy;
        this.modifiedBy = modifiedBy;
    }

    // Getters and Setters
    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getHotelId() {
        return hotelId;
    }

    public void setHotelId(String hotelId) {
        this.hotelId = hotelId;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public Integer getMinCapacity() {
        return minCapacity;
    }
    public Integer getMaxCapacity() {
        return maxCapacity;
    }

    public void setMinCapacity(Integer minCapacity) {
        this.minCapacity = minCapacity;
    }
    public void setMaxCapacity(Integer maxCapacity) {
        this.maxCapacity = maxCapacity;
    }


    public String getAmenities() {
        return amenities;
    }

    public void setAmenities(String amenities) {
        this.amenities = amenities;
    }

    public LocalDateTime getCreateTimestamp() {
        return createTimestamp;
    }

    public void setCreateTimestamp(LocalDateTime createTimestamp) {
        this.createTimestamp = createTimestamp;
    }

    public LocalDateTime getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(LocalDateTime updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }
}
