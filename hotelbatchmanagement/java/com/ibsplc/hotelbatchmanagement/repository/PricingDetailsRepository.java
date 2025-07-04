package com.ibsplc.hotelbatchmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ibsplc.hotelbatchmanagement.entity.PricingDetails;

@Repository
public interface PricingDetailsRepository extends JpaRepository<PricingDetails, String> {
}
