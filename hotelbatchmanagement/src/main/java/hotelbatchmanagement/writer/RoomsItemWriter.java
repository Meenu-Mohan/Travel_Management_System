package hotelbatchmanagement.writer;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import hotelbatchmanagement.entity.Rooms;
import hotelbatchmanagement.repository.RoomsRepository;

@Component
public class RoomsItemWriter implements ItemWriter<Rooms> {

    @Autowired
    private RoomsRepository roomsRepository;

    @Override
    public void write(Chunk<? extends Rooms> chunk) throws Exception {
        roomsRepository.saveAll(chunk.getItems());
    }
}