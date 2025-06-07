package hotelbatchmanagement.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import hotelbatchmanagement.entity.PricingPlan;
import hotelbatchmanagement.exception.BatchProcessingException;

@Component
public class PricingPlanItemProcessor implements ItemProcessor<PricingPlan, PricingPlan> {
    @Override
    public PricingPlan process(PricingPlan pricingPlan) throws Exception {
        // Validate required fields
        if (pricingPlan.getPlanName() == null || pricingPlan.getPlanName().trim().isEmpty()) {
            throw new BatchProcessingException("Plan name cannot be empty");
        }
        if (pricingPlan.getPlanDescription() == null || pricingPlan.getPlanDescription().trim().isEmpty()) {
            throw new BatchProcessingException("Plan Desc cannot be empty");
        }
 
        // Normalize data (e.g., trim and convert to uppercase)
        pricingPlan.setPlanName(pricingPlan.getPlanName().trim().toUpperCase());
        pricingPlan.setPlanDescription(pricingPlan.getPlanDescription().trim().toUpperCase());
 
        // No need to set createdBy/updatedBy; they are handled by @PrePersist/@PreUpdate
        return pricingPlan;
    }
}
