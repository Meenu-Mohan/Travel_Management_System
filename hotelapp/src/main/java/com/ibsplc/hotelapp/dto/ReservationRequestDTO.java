package com.ibsplc.hotelapp.dto;


import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Date;


public class ReservationRequestDTO {
    @NotNull(message = "Hotel Name is required")
    private String hotelName;

    @NotNull(message = "Room Name is required")
    private String roomType;

    @NotNull(message = "Guest Email is required")
    private String email;
    
    @NotNull(message = "Guest Phone Number is required")
    private String phone;
    
    @NotNull(message = "City Name is required")
    private String cityName;

    @NotNull(message = "Check-in date is required")
    //@FutureOrPresent(message = "Check-in date must be today or in the future")
    private Date checkIn;

    @NotNull(message = "Check-out date is required")
    //@FutureOrPresent(message = "Check-out date must be today or in the future")
    private Date checkOut;

    @NotNull(message = "Number of adults is required")
    @Min(value = 1, message = "At least one adult is required")
    private Integer noOfAdult;

    @Min(value = 0, message = "Number of children cannot be negative")
    private Integer noOfChild;

    @NotNull(message = "Status is required")
    private String status;
    
    public ReservationRequestDTO() {
    }

	public String getHotelName() {
		return hotelName;
	}

	public void setHotelName(String hotelName) {
		this.hotelName = hotelName;
	}

	public String getRoomType() {
		return roomType;
	}

	public void setRoomType(String roomType) {
		this.roomType = roomType;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Date getCheckIn() {
		return checkIn;
	}

	public void setCheckIn(Date checkIn) {
		this.checkIn = checkIn;
	}

	public Date getCheckOut() {
		return checkOut;
	}

	public void setCheckOut(Date checkOut) {
		this.checkOut = checkOut;
	}

	public Integer getNoOfAdult() {
		return noOfAdult;
	}

	public void setNoOfAdult(Integer noOfAdult) {
		this.noOfAdult = noOfAdult;
	}

	public Integer getNoOfChild() {
		return noOfChild;
	}

	public void setNoOfChild(Integer noOfChild) {
		this.noOfChild = noOfChild;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
    public String getCityName() { 
    	return cityName; 
    }
    
    public void setCityName(String cityName) { 
    	this.cityName = cityName; 
    }

}
