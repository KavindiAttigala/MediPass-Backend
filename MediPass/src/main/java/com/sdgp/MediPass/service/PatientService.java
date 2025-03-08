package com.sdgp.MediPass.service;

import com.sdgp.MediPass.model.Patient;
import com.sdgp.MediPass.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;
    private PasswordEncoder passwordEncoder;
    public Patient registerPatientAdult(Patient patient) {
        List<Patient> existingPatients = patientRepository.findByNic(patient.getEmail());
        if (existingPatients != null) {
            throw new RuntimeException("Account already exists for this NIC");
        }
        patient.setPassword(passwordEncoder.encode(patient.getPassword())); // Encrypt password
        return patientRepository.save(patient);
    }

    public Patient registerPatientChild(Patient patient){
        List<Patient> existingPatients = patientRepository.findByRoleAndAndNic("Adult", patient.getNic());
        if (existingPatients == null){
            throw new RuntimeException("No account found for this NIC");
        }
        patient.setPassword(passwordEncoder.encode(patient.getPassword())); // Encrypt password
        return patientRepository.save(patient);

    }

    public List<Patient> getUserByMediId(Long mediId) {
        return patientRepository.findByMediId(mediId);
    }

    public Patient updatePatient(Patient patient) {
        return patientRepository.save(patient); // Saves the updated patient in the database
    }
}
