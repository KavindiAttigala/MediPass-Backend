package com.sdgp.MediPass.repository;

import com.sdgp.MediPass.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    List<Patient> findByEmail(String email);
    List<Patient> findByNic(String nic);
}
