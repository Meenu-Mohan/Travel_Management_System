package com.ibsplc.hotelbatchmanagement.writer;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.ibsplc.hotelbatchmanagement.entity.PricingDetails;
import com.ibsplc.hotelbatchmanagement.repository.PricingDetailsRepository;

@Component
public class PricingDetailsItemWriter implements ItemWriter<PricingDetails> {

    @Autowired
    private PricingDetailsRepository pricingDetailsRepository;

    @Override
    public void write(Chunk<? extends PricingDetails> chunk) throws Exception {
        pricingDetailsRepository.saveAll(chunk.getItems());
    }
}
