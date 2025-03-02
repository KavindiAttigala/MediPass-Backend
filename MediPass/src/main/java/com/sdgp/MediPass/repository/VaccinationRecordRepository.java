package com.sdgp.MediPass.repository;

import com.sdgp.MediPass.model.VaccinationRecords;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VaccinationRecordRepository extends JpaRepository <VaccinationRecords, Long> {
}
