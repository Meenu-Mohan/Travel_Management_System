package hotelbatchmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hotelbatchmanagement.entity.Rooms;

@Repository
public interface RoomsRepository extends JpaRepository<Rooms, String> {
}