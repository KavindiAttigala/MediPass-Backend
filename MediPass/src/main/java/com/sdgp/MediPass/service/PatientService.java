package com.sdgp.MediPass.service;

import com.sdgp.MediPass.model.Patient;
import com.sdgp.MediPass.repository.PatientRepository;
import org.springframework.aop.target.LazyInitTargetSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;
    public Patient registerPatientAdult(Patient patient) {
        List<Patient> existingPatients = patientRepository.findByNic(patient.getEmail());
        if (existingPatients != null) {
            throw new RuntimeException("Account already exists for this NIC");
        }
//        user.setPassword(passwordEncoder.encode(user.getPassword())); // Encrypt password
        return patientRepository.save(patient);
    }

    public Patient registerPatientChild(Patient patient){
        List<Patient> existingPatients = patientRepository.findByRoleAndAndNic("Adult", patient.getNic());
        if (existingPatients == null){
            throw new RuntimeException("No account found for this NIC");
        }
//        user.setPassword(passwordEncoder.encode(user.getPassword())); // Encrypt password
        return patientRepository.save(patient);

    }
}
