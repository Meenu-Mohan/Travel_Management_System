package hotelbatchmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hotelbatchmanagement.entity.Hotel;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, String> {

}