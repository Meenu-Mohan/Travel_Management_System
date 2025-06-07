package hotelbatchmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hotelbatchmanagement.entity.RoomAvailability;

@Repository
public interface RoomAvailabilityRepository extends JpaRepository<RoomAvailability, String> {
}
