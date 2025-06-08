package com.ibsplc.flightbatchmanagement.writer;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.ibsplc.flightbatchmanagement.entity.Flight;
import com.ibsplc.flightbatchmanagement.repository.FlightRepository;

@Component
public class FlightItemWriter implements ItemWriter<Flight> {

    @Autowired
    private FlightRepository flightRepository;

    @Override
    public void write(Chunk<? extends Flight> chunk) throws Exception {
        flightRepository.saveAll(chunk.getItems());
    }
}