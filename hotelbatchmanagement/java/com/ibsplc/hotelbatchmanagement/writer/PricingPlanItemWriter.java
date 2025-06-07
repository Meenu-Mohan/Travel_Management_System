package com.ibsplc.hotelbatchmanagement.writer;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.ibsplc.hotelbatchmanagement.entity.PricingPlan;
import com.ibsplc.hotelbatchmanagement.repository.PricingPlanRepository;

@Component
public class PricingPlanItemWriter implements ItemWriter<PricingPlan> {

    @Autowired
    private PricingPlanRepository pricingPlanRepository;

    @Override
    public void write(Chunk<? extends PricingPlan> chunk) throws Exception {
    	pricingPlanRepository.saveAll(chunk.getItems());
    }
}