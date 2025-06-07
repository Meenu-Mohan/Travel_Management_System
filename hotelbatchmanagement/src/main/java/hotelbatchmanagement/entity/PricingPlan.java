package hotelbatchmanagement.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "pricing_plans", schema = "inventory" )
public class PricingPlan {

    @Id
    @Column(name = "plan_id", updatable = false, nullable = false)
    private String planId;

    @Column(name = "plan_name", nullable = false, length = 255)
    private String planName;

    @Column(name = "plan_description", columnDefinition = "TEXT")
    private String planDescription;
    
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
        createdBy = "SYSTEM"; 
        updatedBy = "SYSTEM";
    }

    @PreUpdate
    protected void onUpdate() {
        updateTimestamp = LocalDateTime.now();
        updatedBy = "SYSTEM";
    }

	public PricingPlan() {
		super();
	}
	public PricingPlan(String planId, String planName, String description, LocalDateTime createTimestamp,
			LocalDateTime updateTimestamp, String createdBy, String updatedBy) {
		super();
		this.planId = planId;
		this.planName = planName;
		this.planDescription = description;
		this.createTimestamp = createTimestamp;
		this.updateTimestamp = updateTimestamp;
		this.createdBy = createdBy;
		this.updatedBy = updatedBy;
	}

	public String getPlanId() {
		return planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}

	public String getPlanName() {
		return planName;
	}

	public void setPlanName(String planName) {
		this.planName = planName;
	}

	public String getPlanDescription() {
		return planDescription;
	}

	public void setPlanDescription(String planDescription) {
		this.planDescription = planDescription;
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
