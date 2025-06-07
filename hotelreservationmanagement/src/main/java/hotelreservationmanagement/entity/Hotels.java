package hotelreservationmanagement.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "hotels", schema = "inventory" )
public class Hotels {

    @Id
    @Column(name = "hotel_id", updatable = false, nullable = false)
    private String hotelId;

    @Column(name = "hotel_name", nullable = false, length = 255)
    private String hotelName;

    @Column(name = "description", nullable = false, length = 500)
    private String description;
    
    @Column(name = "address", nullable = false, length = 500)
    private String address;

    @Column(name = "city_id", nullable = false, length = 100)
    private String cityId;

    @Column(name = "rating")
    private Double rating;

    @Column(name = "create_timestamp", nullable = false, updatable = false)
    private LocalDateTime createTimestamp;

    @Column(name = "update_timestamp", nullable = false)
    private LocalDateTime updateTimestamp;

    @Column(name = "created_by", length = 50, nullable = false)
    private String createdBy;

    @Column(name = "updated_by", length = 50, nullable = false)
    private String updatedBy;
    
    @PrePersist
    protected void onCreate() {
        createTimestamp = LocalDateTime.now();
        updateTimestamp = createTimestamp;
        createdBy = "SYSTEM"; 
        updatedBy = "SYSTEM";
    }

    @PreUpdate
    protected void onUpdate() {
        updateTimestamp = LocalDateTime.now();
        updatedBy = "SYSTEM";
    }

	public Hotels() {
		super();
	}

	public Hotels(String hotelId, String hotelName, String description, String address, String cityId, Double rating,
			LocalDateTime createTimestamp, LocalDateTime updateTimestamp, String createdBy, String updatedBy) {
		super();
		this.hotelId = hotelId;
		this.hotelName = hotelName;
		this.description = description;
		this.address = address;
		this.cityId = cityId;
		this.rating = rating;
		this.createTimestamp = createTimestamp;
		this.updateTimestamp = updateTimestamp;
		this.createdBy = createdBy;
		this.updatedBy = updatedBy;
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

    public String getHotelId() {
        return hotelId;
    }

    public void setHotelId(String hotelId) {
        this.hotelId = hotelId;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

	

}
