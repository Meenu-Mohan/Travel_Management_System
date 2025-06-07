package hotelbatchmanagement.reader;


import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import hotelbatchmanagement.entity.PricingDetails;
import hotelbatchmanagement.mapper.PricingDetailsFieldSetMapper;

@Component
public class PricingDetailsItemReader {

    @Bean
    public FlatFileItemReader<PricingDetails> pricingDetailsReader() {
        FlatFileItemReader<PricingDetails> reader = new FlatFileItemReader<>();
        reader.setResource(new ClassPathResource("pricing_details.csv"));
        reader.setLinesToSkip(1); // Skip the header row

        // Set up the line mapper
        DefaultLineMapper<PricingDetails> lineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames("price_id", "room_id", "plan_id", "price"); // Match CSV column names
        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(new PricingDetailsFieldSetMapper());
        reader.setLineMapper(lineMapper);

        return reader;
    }
}
