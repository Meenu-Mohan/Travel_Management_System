// Flight.java
package com.ibsplc.flightreservation.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Flight", schema = "inventory")
public class Flight {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "flight_id")
	private String flightId;

	@Column(name = "carrier_code")
	private String carrierCode;

	@Column(name = "flight_number")
	private String flightNumber;

	@Column(name = "dep_date_time")
	private LocalDateTime depDateTime;

	@Column(name = "dep_airport")
	private String depAirport;

	@Column(name = "arr_airport")
	private String arrAirport;

	@Column(name = "total_no_of_seats")
	private Integer totalNoOfSeats;

	@Column(name = "arr_date_time")
	private LocalDateTime arrDateTime;

	@Column(name = "sector_type")
	private String sectorType;

	@Column(name = "seat_rate")
	private Long seatRate;

	@Column(name = "available_no_of_seats")
	private Integer availableNoOfSeats;

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

	public String getFlightNumber() {
		return flightNumber;
	}

	public void setFlightNumber(String flightNumber) {
		this.flightNumber = flightNumber;
	}

	public LocalDateTime getDepDateTime() {
		return depDateTime;
	}

	public void setDepDateTime(LocalDateTime depDateTime) {
		this.depDateTime = depDateTime;
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

	public Integer getTotalNoOfSeats() {
		return totalNoOfSeats;
	}

	public void setTotalNoOfSeats(Integer totalNoOfSeats) {
		this.totalNoOfSeats = totalNoOfSeats;
	}

	public LocalDateTime getArrDateTime() {
		return arrDateTime;
	}

	public void setArrDateTime(LocalDateTime arrDateTime) {
		this.arrDateTime = arrDateTime;
	}

	public String getSectorType() {
		return sectorType;
	}

	public void setSectorType(String sectorType) {
		this.sectorType = sectorType;
	}

	public Long getSeatRate() {
		return seatRate;
	}

	public void setSeatRate(Long seatRate) {
		this.seatRate = seatRate;
	}

	public Integer getAvailableNoOfSeats() {
		return availableNoOfSeats;
	}

	public void setAvailableNoOfSeats(Integer availableNoOfSeats) {
		this.availableNoOfSeats = availableNoOfSeats;
	}
}