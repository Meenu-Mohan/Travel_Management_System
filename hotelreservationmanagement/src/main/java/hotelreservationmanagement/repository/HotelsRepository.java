package hotelreservationmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hotelreservationmanagement.entity.Hotels;

import java.util.Optional;

@Repository
public interface HotelsRepository extends JpaRepository<Hotels, String> {
	Optional<Hotels> findByHotelName(String hotelName);
    Optional<Hotels> findByHotelId(String hotelId);
}