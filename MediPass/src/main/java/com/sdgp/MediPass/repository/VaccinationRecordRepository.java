package com.sdgp.MediPass.repository;

import com.sdgp.MediPass.model.VaccinationRecords;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VaccinationRecordRepository extends JpaRepository <VaccinationRecords, Long> {
    List<VaccinationRecords> findByMediId(long mediId);
}
