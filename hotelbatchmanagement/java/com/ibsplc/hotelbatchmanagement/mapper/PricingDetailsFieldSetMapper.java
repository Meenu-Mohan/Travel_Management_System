package com.ibsplc.hotelbatchmanagement.mapper;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;
import com.ibsplc.hotelbatchmanagement.entity.PricingDetails;

public class PricingDetailsFieldSetMapper implements FieldSetMapper<PricingDetails> {

    @Override
    public PricingDetails mapFieldSet(FieldSet fieldSet) throws BindException {
        PricingDetails pricingDetails = new PricingDetails();
        pricingDetails.setPriceId(fieldSet.readString("price_id"));
        pricingDetails.setRoomId(fieldSet.readString("room_id"));
        pricingDetails.setPlanId(fieldSet.readString("plan_id"));
        pricingDetails.setPrice(fieldSet.readBigDecimal("price"));
        // Note: We don't map createTimestamp, updateTimestamp, createdBy, modifiedBy
        // because they are set by @PrePersist and @PreUpdate in the PricingDetails entity
        return pricingDetails;
    }
}