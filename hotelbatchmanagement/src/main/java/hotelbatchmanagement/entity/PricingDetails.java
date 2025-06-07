package hotelbatchmanagement.entity;


import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pricing_details", schema = "inventory")
public class PricingDetails {

    @Id
    @Column(name = "price_id", length = 50, nullable = false)
    private String priceId;

    @Column(name = "room_id", length = 50, nullable = false)
    private String roomId;

    @Column(name = "plan_id", length = 50, nullable = false)
    private String planId;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "create_timestamp", nullable = false, updatable = false)
    private LocalDateTime createTimestamp;

    @Column(name = "update_timestamp", nullable = false)
    private LocalDateTime updateTimestamp;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(name = "modified_by", nullable = false)
    private String modifiedBy;

    @PrePersist
    protected void onCreate() {
        createTimestamp = LocalDateTime.now();
        updateTimestamp = createTimestamp;
        createdBy = "SYSTEM";
        modifiedBy = "SYSTEM";
    }

    @PreUpdate
    protected void onUpdate() {
        updateTimestamp = LocalDateTime.now();
        modifiedBy = "SYSTEM";
    }

    // Default constructor
    public PricingDetails() {
        super();
    }

    // Parameterized constructor
    public PricingDetails(String priceId, String roomId, String planId, BigDecimal price,
                         LocalDateTime createTimestamp, LocalDateTime updateTimestamp,
                         String createdBy, String modifiedBy) {
        super();
        this.priceId = priceId;
        this.roomId = roomId;
        this.planId = planId;
        this.price = price;
        this.createTimestamp = createTimestamp;
        this.updateTimestamp = updateTimestamp;
        this.createdBy = createdBy;
        this.modifiedBy = modifiedBy;
    }

    // Getters and Setters
    public String getPriceId() {
        return priceId;
    }

    public void setPriceId(String priceId) {
        this.priceId = priceId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
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