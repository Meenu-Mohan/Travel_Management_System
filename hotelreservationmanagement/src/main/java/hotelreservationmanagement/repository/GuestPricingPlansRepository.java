package hotelreservationmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import hotelreservationmanagement.entity.GuestPricingPlans;

public interface GuestPricingPlansRepository extends JpaRepository<GuestPricingPlans, Long> {
}