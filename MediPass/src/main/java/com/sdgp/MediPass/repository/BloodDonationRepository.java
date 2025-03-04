package com.sdgp.MediPass.repository;

import com.sdgp.MediPass.model.BloodDonationRecords;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BloodDonationRepository extends JpaRepository<BloodDonationRecords, Long> {
}
