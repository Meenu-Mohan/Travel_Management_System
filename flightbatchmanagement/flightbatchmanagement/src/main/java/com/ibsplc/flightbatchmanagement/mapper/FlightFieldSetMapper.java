package com.ibsplc.flightbatchmanagement.mapper;

import java.time.ZoneId;
import java.util.UUID;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;
import com.ibsplc.flightbatchmanagement.entity.Flight;

public class FlightFieldSetMapper implements FieldSetMapper<Flight> {
    @Override
    public Flight mapFieldSet(FieldSet fieldSet) throws BindException {
        Flight flight = new Flight();
        // Generate a new UUID for flightId, ignoring the flight_id from the CSV
        flight.setFlightId(fieldSet.readString("flight_id"));
        // Map the other fields
        flight.setCarrierCode(fieldSet.readString("carrier_code"));
        flight.setFlightNumber(fieldSet.readInt("flight_number"));
        flight.setDepDateTime(fieldSet.readDate("dep_date_time", "yyyy-MM-dd HH:mm:ss").toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        flight.setArrDateTime(fieldSet.readDate("arr_date_time", "yyyy-MM-dd HH:mm:ss").toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        flight.setDepAirport(fieldSet.readString("dep_airport"));
        flight.setArrAirport(fieldSet.readString("arr_airport"));
        flight.setTotalNoOfSeats(fieldSet.readInt("total_no_of_seats"));
        flight.setSectorType(fieldSet.readString("sector_type"));
        flight.setSeatRate(fieldSet.readInt("seat_rate"));
        flight.setAvailableNoOfSeats(fieldSet.readInt("available_no_of_seats"));
        // Note: We don't map createTimestamp, updateTimestamp, createdBy, updatedBy
        // because they are set by @PrePersist and @PreUpdate in the Flight entity
        return flight;
    }
}