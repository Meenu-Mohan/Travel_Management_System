package hotelbatchmanagement.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import hotelbatchmanagement.entity.PricingDetails;
import hotelbatchmanagement.exception.BatchProcessingException;

@Component
public class PricingDetailsItemProcessor implements ItemProcessor<PricingDetails, PricingDetails> {

    @Override
    public PricingDetails process(PricingDetails pricingDetails) throws Exception {
        // Validate required fields
        if (pricingDetails.getPriceId() == null || pricingDetails.getPriceId().trim().isEmpty()) {
            throw new BatchProcessingException("Price ID cannot be empty");
        }
        if (pricingDetails.getRoomId() == null || pricingDetails.getRoomId().trim().isEmpty()) {
            throw new BatchProcessingException("Room ID cannot be empty");
        }
        if (pricingDetails.getPlanId() == null || pricingDetails.getPlanId().trim().isEmpty()) {
            throw new BatchProcessingException("Plan ID cannot be empty");
        }
        if (pricingDetails.getPrice() == null) {
            throw new BatchProcessingException("Price cannot be null");
        }

        // Normalize data (e.g., trim IDs)
        pricingDetails.setPriceId(pricingDetails.getPriceId().trim());
        pricingDetails.setRoomId(pricingDetails.getRoomId().trim());
        pricingDetails.setPlanId(pricingDetails.getPlanId().trim());

        // No need to set createTimestamp, updateTimestamp, createdBy, modifiedBy
        // They are handled by @PrePersist/@PreUpdate in the PricingDetails entity
        return pricingDetails;
    }
}
