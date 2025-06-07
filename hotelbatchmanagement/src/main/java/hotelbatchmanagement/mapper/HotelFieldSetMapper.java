package hotelbatchmanagement.mapper;
 
import hotelbatchmanagement.entity.Hotel;
import hotelbatchmanagement.mapper.HotelFieldSetMapper;

import org.springframework.batch.item.file.mapping.FieldSetMapper;

import org.springframework.batch.item.file.transform.FieldSet;

import org.springframework.validation.BindException;
 

public class HotelFieldSetMapper implements FieldSetMapper<Hotel> {
	    @Override
	    public Hotel mapFieldSet(FieldSet fieldSet) throws BindException {
	        Hotel hotel = new Hotel();
	        hotel.setHotelId(fieldSet.readString("hotel_id"));
	        hotel.setHotelName(fieldSet.readString("hotel_name")); 
	        hotel.setDescription(fieldSet.readString("description"));
	        hotel.setAddress(fieldSet.readString("address"));
	        hotel.setCityId(fieldSet.readString("city_id"));
	        hotel.setRating(fieldSet.readDouble("rating"));
	      
	        return hotel;
	    }
	}

 