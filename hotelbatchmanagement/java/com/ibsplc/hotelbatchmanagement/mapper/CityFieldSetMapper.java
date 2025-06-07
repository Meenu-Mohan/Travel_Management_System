package com.ibsplc.hotelbatchmanagement.mapper;

import java.time.LocalDateTime;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;
import com.ibsplc.hotelbatchmanagement.entity.City;

public class CityFieldSetMapper implements FieldSetMapper<City> {
    @Override
    public City mapFieldSet(FieldSet fieldSet) throws BindException {
        City city = new City();
        // Map the other fields
        city.setCityId(fieldSet.readString("city_id"));
        city.setCityName(fieldSet.readString("name")); // CSV uses "name", entity uses "city_name"
        city.setState(fieldSet.readString("state"));
        city.setCountry(fieldSet.readString("country"));
        
        if (city.getCreatedBy() == null) {
            city.setCreatedBy("SYSTEM");
        }
        if (city.getUpdatedBy() == null) {
            city.setUpdatedBy("SYSTEM");
        }
        if (city.getCreateTimestamp() == null) {
            city.setCreateTimestamp(LocalDateTime.now());
        }
        if (city.getUpdateTimestamp() == null) {
            city.setUpdateTimestamp(LocalDateTime.now());
        }
        
        return city;
    }
}