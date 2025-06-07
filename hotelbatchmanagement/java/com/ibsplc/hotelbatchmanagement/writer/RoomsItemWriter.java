package com.ibsplc.hotelbatchmanagement.writer;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.ibsplc.hotelbatchmanagement.entity.Rooms;
import com.ibsplc.hotelbatchmanagement.repository.RoomsRepository;

@Component
public class RoomsItemWriter implements ItemWriter<Rooms> {

    @Autowired
    private RoomsRepository roomsRepository;

    @Override
    public void write(Chunk<? extends Rooms> chunk) throws Exception {
        roomsRepository.saveAll(chunk.getItems());
    }
}