// PassengerDetailsDTO.java
package commonmodule.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PassengerDetailsDTO {
	private Integer passengerId;
	private String passengerName;
	private Integer age;
	private Long phoneNumber;
	private String emailAddress;
	private LocalDateTime dateOfBirth;
	private String passportNumber;
	private Integer noOfTickets;
	private Integer reservationId;

	public Integer getPassengerId() {
		return passengerId;
	}

	public void setPassengerId(Integer passengerId) {
		this.passengerId = passengerId;
	}

	public String getPassengerName() {
		return passengerName;
	}

	public void setPassengerName(String passengerName) {
		this.passengerName = passengerName;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Long getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(Long phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public LocalDateTime getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDateTime dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getPassportNumber() {
		return passportNumber;
	}

	public void setPassportNumber(String passportNumber) {
		this.passportNumber = passportNumber;
	}

	public Integer getNoOfTickets() {
		return noOfTickets;
	}

	public void setNoOfTickets(Integer noOfTickets) {
		this.noOfTickets = noOfTickets;
	}

	public Integer getReservationId() {
		return reservationId;
	}

	public void setReservationId(Integer reservationId) {
		this.reservationId = reservationId;
	}
	
	@Override
    public String toString() {
        return "Passenger Details: " +
               "Passenger ID=" + passengerId +
               ", Name=" + passengerName +
               ", Age=" + age +
               ", Phone Number=" + phoneNumber +
               ", Email=" + emailAddress +
               ", Date of Birth=" + dateOfBirth +
               ", Passport Number=" + passportNumber +
               ", Number of Tickets=" + noOfTickets +
               ", Reservation ID=" + reservationId;
    }
}