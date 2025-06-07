package hotelreservationmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import hotelreservationmanagement.entity.GuestReservationPlans;

public interface GuestReservationPlansRepository extends JpaRepository<GuestReservationPlans, Long> {
}