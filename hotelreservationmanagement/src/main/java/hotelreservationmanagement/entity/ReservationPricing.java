package hotelreservationmanagement.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservation_pricing", schema = "reservation")

public class ReservationPricing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pricing_id")
    private Long pricingId;

    @ManyToOne
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservations reservation;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "create_timestamp")
    private LocalDateTime createTimestamp;

    @Column(name = "update_timestamp")
    private LocalDateTime updateTimestamp;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "modified_by")
    private String modifiedBy;

	public Long getPricingId() {
		return pricingId;
	}

	public void setPricingId(Long pricingId) {
		this.pricingId = pricingId;
	}

	public Reservations getReservation() {
		return reservation;
	}

	public void setReservation(Reservations reservation) {
		this.reservation = reservation;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
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