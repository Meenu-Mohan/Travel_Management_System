package hotelreservationmanagement.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "reservations", schema = "reservation")

public class Reservations {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Long reservationId;

    @Column(name = "hotel_id", nullable = false)
    private String hotelId; // Foreign key to Hotels (in hotelbatchmanagement project)

    @ManyToOne
    @JoinColumn(name = "guest_id", nullable = false)
    private Guests guest; // Foreign key to Guests (in this project)

    @Column(name = "room_id", nullable = false)
    private String roomId; // Foreign key to Rooms (in hotelbatchmanagement project)

    @Column(name = "check_in")
    private Date checkIn;

    @Column(name = "check_out")
    private Date checkOut;

    @Column(name = "no_of_adult")
    private Integer noOfAdult;

    @Column(name = "no_of_child")
    private Integer noOfChild;

    @Column(name = "status")
    private String status;

    @Column(name = "create_timestamp")
    private LocalDateTime createTimestamp;

    @Column(name = "update_timestamp")
    private LocalDateTime updateTimestamp;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "modified_by")
    private String modifiedBy;

	public Long getReservationId() {
		return reservationId;
	}

	public void setReservationId(Long reservationId) {
		this.reservationId = reservationId;
	}

	public String getHotelId() {
		return hotelId;
	}

	public void setHotelId(String hotelId) {
		this.hotelId = hotelId;
	}

	public Guests getGuest() {
		return guest;
	}

	public void setGuest(Guests guest) {
		this.guest = guest;
	}

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
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