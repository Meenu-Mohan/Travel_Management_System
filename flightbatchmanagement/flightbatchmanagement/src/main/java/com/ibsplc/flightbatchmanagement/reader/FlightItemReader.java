package com.ibsplc.flightbatchmanagement.reader;

import com.ibsplc.flightbatchmanagement.entity.Flight;
import com.ibsplc.flightbatchmanagement.mapper.FlightFieldSetMapper;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class FlightItemReader {

    public FlatFileItemReader<Flight> flightReader() {
        FlatFileItemReader<Flight> reader = new FlatFileItemReader<>();
        reader.setResource(new ClassPathResource("flightdata.csv"));
        reader.setLinesToSkip(1); // Skip the header row

        // Set up the line mapper
        DefaultLineMapper<Flight> lineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames("flight_id", "carrier_code", "flight_number", "dep_date_time", "arr_date_time", "dep_airport", "arr_airport", "total_no_of_seats", "sector_type", "seat_rate", "available_no_of_seats"); // Match CSV column names
        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(new FlightFieldSetMapper());
        reader.setLineMapper(lineMapper);

        return reader;
    }
}