package com.ibsplc.hotelbatchmanagement.reader;

import com.ibsplc.hotelbatchmanagement.entity.City;
import com.ibsplc.hotelbatchmanagement.mapper.CityFieldSetMapper;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;


@Component
public class CityItemReader {

    public FlatFileItemReader<City> cityReader() {
        FlatFileItemReader<City> reader = new FlatFileItemReader<>();
        reader.setResource(new ClassPathResource("cities.csv"));
        reader.setLinesToSkip(1); // Skip the header row

        // Set up the line mapper
        DefaultLineMapper<City> lineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames("city_id", "name", "state", "country"); // Match CSV column names
        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(new CityFieldSetMapper());
        reader.setLineMapper(lineMapper);

        return reader;
    }
}