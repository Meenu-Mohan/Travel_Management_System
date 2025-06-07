package hotelreservationmanagement.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import hotelreservationmanagement.entity.ReservationPricing;
import hotelreservationmanagement.entity.Reservations;

public interface ReservationPricingRepository extends JpaRepository<ReservationPricing, Long> {

    @Query("SELECT rp FROM ReservationPricing rp WHERE rp.reservation = :reservation")
    Optional<ReservationPricing> findByReservation(@Param("reservation") Reservations reservation);
}