package com.sdgp.MediPass.repository;

import com.sdgp.MediPass.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    List<Patient> findByNic(String nic);

    // Renamed method to match the service call
    List<Patient> findByRoleAndNic(String role, String nic);

    Optional<Patient> findByNicAndMediId(String nic, long mediId);

    Optional<Patient> findByMediId(long mediId);


}
