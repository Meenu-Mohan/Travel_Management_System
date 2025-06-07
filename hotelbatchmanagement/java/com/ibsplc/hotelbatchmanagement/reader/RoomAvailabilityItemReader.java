package com.ibsplc.hotelbatchmanagement.reader;

import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import com.ibsplc.hotelbatchmanagement.entity.RoomAvailability;
import com.ibsplc.hotelbatchmanagement.mapper.RoomAvailabilityFieldSetMapper;

@Component
public class RoomAvailabilityItemReader {

    @Bean
    public FlatFileItemReader<RoomAvailability> roomAvailabilityReader() {
        FlatFileItemReader<RoomAvailability> reader = new FlatFileItemReader<>();
        reader.setResource(new ClassPathResource("room_availability.csv")); // Adjust the file name as needed
        reader.setLinesToSkip(1); // Skip the header row

        // Set up the line mapper
        DefaultLineMapper<RoomAvailability> lineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames("availability_id", "room_id", "date", "available_stock", "used_stock", "total_stock"); 
        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(new RoomAvailabilityFieldSetMapper()); // Use a custom FieldSetMapper
        reader.setLineMapper(lineMapper);

        return reader;
    }
}