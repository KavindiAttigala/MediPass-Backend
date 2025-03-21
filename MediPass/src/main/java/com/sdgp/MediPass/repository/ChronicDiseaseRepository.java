package com.sdgp.MediPass.repository;

import com.sdgp.MediPass.model.ChronicDisease;
import com.sdgp.MediPass.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChronicDiseaseRepository extends JpaRepository<ChronicDisease, Integer> {
    List<ChronicDisease> findByPatient(Patient patient);

    List<ChronicDisease> findByMediIdAndDisease(long mediId, String diseaseName);
}
