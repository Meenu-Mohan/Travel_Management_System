package hotelreservationmanagement.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Date;


@Entity
@Table(name = "room_availability", schema = "inventory")
public class RoomAvailability {

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

	public void setAvailabilityStock(Integer availabilityStock) {
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

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	@Id
    @Column(name = "availability_id", updatable = false, nullable = false)
    private String availabilityId;

    @Column(name = "room_id")
    private String roomId;

    @Column(name = "date", nullable = false)
    private Date date;

    @Column(name = "availability_stock", nullable = false)
    private Integer availabilityStock;

    @Column(name = "used_stock", nullable = false)
    private Integer usedStock;

    @Column(name = "total_stock", nullable = false)
    private Integer totalStock;

    @Column(name = "create_timestamp", nullable = false, updatable = false)
    private LocalDateTime createTimestamp;

    @Column(name = "update_timestamp", nullable = false)
    private LocalDateTime updateTimestamp;

    @Column(name = "created_by", length = 50, nullable = false)
    private String createdBy;

    @Column(name = "updated_by", length = 50, nullable = false)
    private String updatedBy;
}