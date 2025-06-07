package hotelreservationmanagement.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import hotelreservationmanagement.entity.RoomAvailability;

import java.util.Date;
import java.util.Optional;

public interface RoomAvailabilityRepository extends JpaRepository<RoomAvailability, String> {
	 

	Optional<RoomAvailability> findByRoomIdAndDate(String roomId, Date currentDate);

}
 