package com.ibsplc.hotelbatchmanagement.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import com.ibsplc.hotelbatchmanagement.entity.City;
import com.ibsplc.hotelbatchmanagement.exception.BatchProcessingException;

@Component
public class CityItemProcessor implements ItemProcessor<City, City> {
    @Override
    public City process(City city) throws Exception {
        // Validate required fields
        if (city.getCityName() == null || city.getCityName().trim().isEmpty()) {
            throw new BatchProcessingException("City name cannot be empty");
        }
        if (city.getState() == null || city.getState().trim().isEmpty()) {
            throw new BatchProcessingException("State cannot be empty");
        }
        if (city.getCountry() == null || city.getCountry().trim().isEmpty()) {
            throw new BatchProcessingException("Country cannot be empty");
        }

        // Normalize data (e.g., trim and convert to uppercase)
        city.setCityName(city.getCityName().trim().toUpperCase());
        city.setState(city.getState().trim().toUpperCase());
        city.setCountry(city.getCountry().trim().toUpperCase());

        // No need to set createdBy/updatedBy; they are handled by @PrePersist/@PreUpdate
        return city;
    }
}
