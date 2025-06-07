package hotelavailability.entity;

import java.sql.Date;
import java.sql.Timestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class RoomAvailability {
	
    @Id
    private String availabilityId;
    private String roomId;
    private Date date;
    private Integer availabilityStock;
    private Integer usedStock;
    private Integer totalStock;
    private Timestamp createTimestamp;
    private Timestamp updateTimestamp;
    private String createdBy;
    private String modifiedBy;
    

    // Getters and Setters
	public String getAvailabilityId() {
		return availabilityId;
	}
	public void setAvailabilityId(String availabilityId) {
		this.availabilityId = availabilityId;
	}
	public String getRoomId() {
		return roomId;
	}
	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Integer getAvailabilityStock() {
		return availabilityStock;
	}
	public void setAvailablilityStock(Integer availabilityStock) {
		this.availabilityStock = availabilityStock;
	}
	public Integer getUsedStock() {
		return usedStock;
	}
	public void setUsedStock(Integer usedStock) {
		this.usedStock = usedStock;
	}
	public Integer getTotalStock() {
		return totalStock;
	}
	public void setTotalStock(Integer totalStock) {
		this.totalStock = totalStock;
	}
	public Timestamp getCreateTimestamp() {
		return createTimestamp;
	}
	public void setCreateTimestamp(Timestamp createTimestamp) {
		this.createTimestamp = createTimestamp;
	}
	public Timestamp getUpdateTimestamp() {
		return updateTimestamp;
	}
	public void setUpdateTimestamp(Timestamp updateTimestamp) {
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