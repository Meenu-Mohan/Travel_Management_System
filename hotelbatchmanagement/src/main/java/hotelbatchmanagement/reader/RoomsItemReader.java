package hotelbatchmanagement.reader;

import hotelbatchmanagement.entity.Rooms;
import hotelbatchmanagement.mapper.RoomsFieldSetMapper;

import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class RoomsItemReader {

    public FlatFileItemReader<Rooms> roomsReader() {
        FlatFileItemReader<Rooms> reader = new FlatFileItemReader<>();
        reader.setResource(new ClassPathResource("rooms.csv"));
        reader.setLinesToSkip(1); // Skip the header row

        // Set up the line mapper
        DefaultLineMapper<Rooms> lineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames("room_id", "hotel_id", "room_type", "min_capacity","max_capacity", "amenities"); // Match CSV column names
        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(new RoomsFieldSetMapper());
        reader.setLineMapper(lineMapper);

        return reader;
    }
}