package com.ibsplc.hotelbatchmanagement.writer;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.ibsplc.hotelbatchmanagement.entity.RoomAvailability;
import com.ibsplc.hotelbatchmanagement.repository.RoomAvailabilityRepository;

@Component
public class RoomAvailabilityItemWriter implements ItemWriter<RoomAvailability>
{
	 @Autowired
	    private RoomAvailabilityRepository roomAvailabilityRepository;

	    @Override
	    public void write(Chunk<? extends RoomAvailability> chunk) throws Exception {
	    	roomAvailabilityRepository.saveAll(chunk.getItems());
	    }
}

