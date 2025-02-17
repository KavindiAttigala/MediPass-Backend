package com.sdgp.MediPass.repository;

import com.sdgp.MediPass.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    List<Patient> findByEmail(String email);
    List<Patient> findByNic(String nic);
    Optional<Patient> findByNicAndMediId(String nic, String mediId);
}
