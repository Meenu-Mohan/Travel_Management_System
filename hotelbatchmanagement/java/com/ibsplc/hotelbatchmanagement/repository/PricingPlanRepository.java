package com.ibsplc.hotelbatchmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ibsplc.hotelbatchmanagement.entity.PricingPlan;

@Repository
public interface PricingPlanRepository extends JpaRepository<PricingPlan, String> {
}