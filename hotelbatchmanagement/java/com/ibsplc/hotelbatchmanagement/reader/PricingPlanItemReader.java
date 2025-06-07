package com.ibsplc.hotelbatchmanagement.reader;

import com.ibsplc.hotelbatchmanagement.entity.PricingPlan;
import com.ibsplc.hotelbatchmanagement.mapper.PricingPlanFieldSetMapper;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class PricingPlanItemReader {
 
    public FlatFileItemReader<PricingPlan> pricingPlanReader() {
        FlatFileItemReader<PricingPlan> reader = new FlatFileItemReader<>();
        reader.setResource(new ClassPathResource("pricing_plans.csv"));
        reader.setLinesToSkip(1); // Skip the header row
 
        // Set up the line mapper
        DefaultLineMapper<PricingPlan> lineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames("plan_id", "plan_name", "plan_description"); // Match CSV column names
        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(new PricingPlanFieldSetMapper());
        reader.setLineMapper(lineMapper);
 
        return reader;
    }
}