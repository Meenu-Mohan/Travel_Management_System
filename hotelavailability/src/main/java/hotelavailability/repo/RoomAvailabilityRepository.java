package hotelavailability.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import hotelavailability.entity.RoomAvailability;

import java.util.Date;
import java.util.List;

@Repository
public interface RoomAvailabilityRepository extends JpaRepository<RoomAvailability, String> {
	 @Query(value = "SELECT * FROM inventory.room_availability r " +
             "WHERE r.room_id IN :roomIds " +
             "AND r.date BETWEEN :startDate AND :endDate " +
             "AND r.availability_stock > 0", nativeQuery = true)   
	 List<RoomAvailability> findByRoomIdsAndDateBetween(
            @Param("roomIds") List<String> roomIds,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate
    );
}