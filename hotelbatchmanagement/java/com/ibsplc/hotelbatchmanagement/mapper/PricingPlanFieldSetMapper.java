package com.ibsplc.hotelbatchmanagement.mapper;
 
import com.ibsplc.hotelbatchmanagement.entity.PricingPlan;

import org.springframework.batch.item.file.mapping.FieldSetMapper;

import org.springframework.batch.item.file.transform.FieldSet;

import org.springframework.validation.BindException;
import com.ibsplc.hotelbatchmanagement.mapper.PricingPlanFieldSetMapper;
 

public class PricingPlanFieldSetMapper implements FieldSetMapper<PricingPlan> {
	    @Override
	    public PricingPlan mapFieldSet(FieldSet fieldSet) throws BindException {
	    	PricingPlan pricingPlan = new PricingPlan();
	        
	    	pricingPlan.setPlanId(fieldSet.readString("plan_id"));
	    	pricingPlan.setPlanName(fieldSet.readString("plan_name")); // CSV uses "name", entity uses "city_name"
	    	pricingPlan.setPlanDescription(fieldSet.readString("plan_description"));
	        // Note: We don't map createTimestamp, updateTimestamp, createdBy, updatedBy
	        // because they are set by @PrePersist and @PreUpdate in the City entity
	        return pricingPlan;
	    }
	}

 