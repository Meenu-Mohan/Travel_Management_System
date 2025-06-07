package hotelreservationmanagement.dto;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;


public class ReservationResponseDTO {
    private Long reservationId;
    private String hotelName;
    private String roomType;
    private String guestFirstName;
    private String guestLastName;
    private Date checkIn;
    private Date checkOut;
    private Integer noOfAdult;
    private Integer noOfChild;
    private String planName;
    public String getPlanName() {
		return planName;
	}
	public void setPlanName(String planName) {
		this.planName = planName;
	}
	private String status;   
    private BigDecimal price;
	public Long getReservationId() {
		return reservationId;
	}
	public void setReservationId(Long reservationId) {
		this.reservationId = reservationId;
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


	public String getGuestFirstName() {
		return guestFirstName;
	}

	public void setGuestFirstName(String guestFirstName) {
		this.guestFirstName = guestFirstName;
	}
	
	public String getGuestLastName() {
		return guestLastName;
	}

	public void setGuestLastName(String guestLastName) {
		this.guestLastName = guestLastName;
	
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
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
}