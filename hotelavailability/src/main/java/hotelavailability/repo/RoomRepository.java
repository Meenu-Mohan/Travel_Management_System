package hotelavailability.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hotelavailability.entity.Rooms;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Rooms, String> {
    List<Rooms> findByHotelId(String hotelId); // Find rooms by hotelId
}