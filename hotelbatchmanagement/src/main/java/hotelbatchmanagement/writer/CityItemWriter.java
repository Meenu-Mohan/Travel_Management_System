package hotelbatchmanagement.writer;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import hotelbatchmanagement.entity.City;
import hotelbatchmanagement.repository.CityRepository;

@Component
public class CityItemWriter implements ItemWriter<City> {

    @Autowired
    private CityRepository cityRepository;

    @Override
    public void write(Chunk<? extends City> chunk) throws Exception {
        cityRepository.saveAll(chunk.getItems());
    }
}