package hotelreservationmanagement.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import hotelreservationmanagement.entity.ReservationPlans;
import hotelreservationmanagement.entity.Reservations;

public interface ReservationPlansRepository extends JpaRepository<ReservationPlans, Long> {
	 @Query("SELECT rp FROM ReservationPlans rp WHERE rp.reservation = :reservation")
	    Optional<ReservationPlans> findByReservation(@Param("reservation") Reservations reservation);
}