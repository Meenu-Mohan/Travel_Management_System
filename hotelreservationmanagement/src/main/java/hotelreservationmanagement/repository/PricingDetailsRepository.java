package hotelreservationmanagement.repository;
 
import java.util.Optional;
 
import org.springframework.data.jpa.repository.JpaRepository;

import hotelreservationmanagement.entity.Guests;
import hotelreservationmanagement.entity.PricingDetails;
 
public interface PricingDetailsRepository extends JpaRepository<PricingDetails, String> {
 
	Optional<PricingDetails> findByRoomIdAndPlanId(String roomId, String planId);
 
}
 