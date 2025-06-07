package hotelbatchmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hotelbatchmanagement.entity.PricingPlan;

@Repository
public interface PricingPlanRepository extends JpaRepository<PricingPlan, String> {
}