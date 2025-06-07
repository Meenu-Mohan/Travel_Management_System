package hotelreservationmanagement.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import hotelreservationmanagement.entity.Reservations;

public interface ReservationsRepository extends JpaRepository<Reservations, Long> {
	@Query("SELECT r FROM Reservations r JOIN r.guest g WHERE g.email = :email AND g.phone = :phone")
    List<Reservations> findByGuestEmailAndPhone(@Param("email") String email, @Param("phone") String phone);
}