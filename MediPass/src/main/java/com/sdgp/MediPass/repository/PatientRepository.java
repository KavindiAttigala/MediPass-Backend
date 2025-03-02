package com.sdgp.MediPass.repository;

import com.sdgp.MediPass.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    List<Patient> findByMediId(long mediId);
    List<Patient> findByNic(String nic);
    List<Patient> findByRoleAndAndNic(String role, String nic);
}
