package hotelreservationmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hotelreservationmanagement.entity.Rooms;

import java.util.Optional;


@Repository
public interface RoomsRepository extends JpaRepository<Rooms, String> {

    Optional<Rooms> findByRoomTypeAndHotelId(String roomType, String hotelId);
    Optional<Rooms> findByRoomId(String roomId);
}