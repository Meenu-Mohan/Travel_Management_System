package com.ibsplc.hotelbatchmanagement.entity;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


@Entity
@Table(name = "Cities", schema = "inventory")
public class City {

    @Id
    @Column(name = "city_id")
    private String cityId;

    @Column(name = "city_name", length = 50, nullable = false)
    @NotNull
    @Size(max = 50)
    private String cityName;

    @Column(name = "state", length = 50, nullable = false)
    @NotNull
    @Size(max = 50)
    private String state;

    @Column(name = "country", length = 50, nullable = false)
    @NotNull
    @Size(max = 50)
    private String country;

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
        createdBy = "SYSTEM"; // Set to "SYSTEM" on creation
        updatedBy = "SYSTEM"; // Set to "SYSTEM" on creation
    }

    @PreUpdate
    protected void onUpdate() {
        updateTimestamp = LocalDateTime.now();
        updatedBy = "SYSTEM"; // Set to "SYSTEM" on update
    }
    
    

	public City() {
		super();
	}

	public City(String cityId, @NotNull @Size(max = 50) String cityName, @NotNull @Size(max = 50) String state,
			@NotNull @Size(max = 50) String country, LocalDateTime createTimestamp, LocalDateTime updateTimestamp,
			String createdBy, String updatedBy) {
		super();
		this.cityId = cityId;
		this.cityName = cityName;
		this.state = state;
		this.country = country;
		this.createTimestamp = createTimestamp;
		this.updateTimestamp = updateTimestamp;
		this.createdBy = createdBy;
		this.updatedBy = updatedBy;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
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
    
}