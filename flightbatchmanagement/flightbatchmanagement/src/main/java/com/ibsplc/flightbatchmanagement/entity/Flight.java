package com.ibsplc.flightbatchmanagement.entity;

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
@Table(name = "Flight", schema = "inventory")
public class Flight {

    @Id
    @Column(name = "flight_id")
    private String flightId;

    @Column(name = "carrier_code", length = 10, nullable = false)
    @NotNull
    @Size(max = 10)
    private String carrierCode;

    @Column(name = "flight_number", nullable = false)
    @NotNull
    private int flightNumber;

    @Column(name = "dep_date_time", nullable = false)
    @NotNull
    private LocalDateTime depDateTime;

    @Column(name = "arr_date_time", nullable = false)
    @NotNull
    private LocalDateTime arrDateTime;

    @Column(name = "dep_airport", length = 10, nullable = false)
    @NotNull
    @Size(max = 10)
    private String depAirport;

    @Column(name = "arr_airport", length = 10, nullable = false)
    @NotNull
    @Size(max = 10)
    private String arrAirport;

    @Column(name = "total_no_of_seats", nullable = false)
    @NotNull
    private int totalNoOfSeats;

    @Column(name = "sector_type", length = 20, nullable = false)
    @NotNull
    @Size(max = 20)
    private String sectorType;

    @Column(name = "seat_rate", nullable = false)
    @NotNull
    private int seatRate;

    @Column(name = "available_no_of_seats", nullable = false)
    @NotNull
    private int availableNoOfSeats;

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

    // Constructors, Getters, and Setters

    public Flight() {
        super();
    }

    public Flight(String flightId, @NotNull @Size(max = 10) String carrierCode, @NotNull int flightNumber,
                  @NotNull LocalDateTime depDateTime, @NotNull LocalDateTime arrDateTime,
                  @NotNull @Size(max = 10) String depAirport, @NotNull @Size(max = 10) String arrAirport,
                  @NotNull int totalNoOfSeats, @NotNull @Size(max = 20) String sectorType,
                  @NotNull int seatRate, @NotNull int availableNoOfSeats, LocalDateTime createTimestamp,
                  LocalDateTime updateTimestamp, String createdBy, String updatedBy) {
        super();
        this.flightId = flightId;
        this.carrierCode = carrierCode;
        this.flightNumber = flightNumber;
        this.depDateTime = depDateTime;
        this.arrDateTime = arrDateTime;
        this.depAirport = depAirport;
        this.arrAirport = arrAirport;
        this.totalNoOfSeats = totalNoOfSeats;
        this.sectorType = sectorType;
        this.seatRate = seatRate;
        this.availableNoOfSeats = availableNoOfSeats;
        this.createTimestamp = createTimestamp;
        this.updateTimestamp = updateTimestamp;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
    }

    // Getters and Setters

    public String getFlightId() {
        return flightId;
    }

    public void setFlightId(String flightId) {
        this.flightId = flightId;
    }

    public String getCarrierCode() {
        return carrierCode;
    }

    public void setCarrierCode(String carrierCode) {
        this.carrierCode = carrierCode;
    }

    public int getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(int flightNumber) {
        this.flightNumber = flightNumber;
    }

    public LocalDateTime getDepDateTime() {
        return depDateTime;
    }

    public void setDepDateTime(LocalDateTime depDateTime) {
        this.depDateTime = depDateTime;
    }

    public LocalDateTime getArrDateTime() {
        return arrDateTime;
    }

    public void setArrDateTime(LocalDateTime arrDateTime) {
        this.arrDateTime = arrDateTime;
    }

    public String getDepAirport() {
        return depAirport;
    }

    public void setDepAirport(String depAirport) {
        this.depAirport = depAirport;
    }

    public String getArrAirport() {
        return arrAirport;
    }

    public void setArrAirport(String arrAirport) {
        this.arrAirport = arrAirport;
    }

    public int getTotalNoOfSeats() {
        return totalNoOfSeats;
    }

    public void setTotalNoOfSeats(int totalNoOfSeats) {
        this.totalNoOfSeats = totalNoOfSeats;
    }

    public String getSectorType() {
        return sectorType;
    }

    public void setSectorType(String sectorType) {
        this.sectorType = sectorType;
    }

    public void setSeatRate(int seatRate) {
        this.seatRate = seatRate;
    }

    public int getSeatRate() {
		return seatRate;
	}

	public int getAvailableNoOfSeats() {
        return availableNoOfSeats;
    }

    public void setAvailableNoOfSeats(int availableNoOfSeats) {
        this.availableNoOfSeats = availableNoOfSeats;
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