package com.ibsplc.hotelbatchmanagement.writer;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.ibsplc.hotelbatchmanagement.entity.Hotel;
import com.ibsplc.hotelbatchmanagement.repository.HotelRepository;

@Component
public class HotelItemWriter implements ItemWriter<Hotel> {

    @Autowired
    private HotelRepository hotelRepository;

    @Override
    public void write(Chunk<? extends Hotel> chunk) throws Exception {
    	hotelRepository.saveAll(chunk.getItems());
    }
}