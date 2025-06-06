// FlightDTO.java
package commonmodule.dto;

import java.time.LocalDateTime;
import lombok.Data;
@Data
public class FlightDTO {
    private String flightId;
    private String carrierCode;
    private String flightNumber;
    private LocalDateTime depDateTime;
    private String depAirport;
    private String arrAirport;
    private Integer totalNoOfSeats;
    private LocalDateTime arrDateTime;
    private String sectorType;
    private Long seatRate;
    private Integer availableNoOfSeats;
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
    
	@Override
    public String toString() {
        return "Flight Details: " +
               "Flight ID=" + flightId +
               ", Carrier Code=" + carrierCode +
               ", Flight Number=" + flightNumber +
               ", Departure=" + depDateTime +
               ", From=" + depAirport +
               ", To=" + arrAirport +
               ", Arrival=" + arrDateTime +
               ", Total Seats=" + totalNoOfSeats +
               ", Available Seats=" + availableNoOfSeats +
               ", Sector Type=" + sectorType +
               ", Seat Rate=" + seatRate;
    }
}