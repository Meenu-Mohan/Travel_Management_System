package com.ibsplc.hotelbatchmanagement.reader;

import com.ibsplc.hotelbatchmanagement.entity.Hotel;
import com.ibsplc.hotelbatchmanagement.mapper.HotelFieldSetMapper;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class HotelItemReader {
 
    public FlatFileItemReader<Hotel> hotelReader() {
        FlatFileItemReader<Hotel> reader = new FlatFileItemReader<>();
        reader.setResource(new ClassPathResource("hotels.csv"));
        reader.setLinesToSkip(1); // Skip the header row
 
        // Set up the line mapper
        DefaultLineMapper<Hotel> lineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames("hotel_id", "hotel_name", "description", "address", "city_id", "rating");
        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(new HotelFieldSetMapper());
        reader.setLineMapper(lineMapper);
 
        return reader;
    }
}