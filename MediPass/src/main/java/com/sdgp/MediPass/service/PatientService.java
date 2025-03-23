package com.sdgp.MediPass.service;

import com.sdgp.MediPass.model.Patient;
import com.sdgp.MediPass.repository.PatientRepository;
import com.sdgp.MediPass.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Patient registerPatientAdult(Patient patient) {
        // Assuming NIC is the unique identifier. Adjust if needed.
        List<Patient> existingPatients = patientRepository.findByNic(patient.getNic());
        if (existingPatients != null && !existingPatients.isEmpty()) {
            throw new RuntimeException("Account already exists for this NIC");
        }
        patient.setPassword(passwordEncoder.encode(patient.getPassword())); // Encrypt password
        return patientRepository.save(patient);
    }

    public Patient registerPatientChild(Patient patient) {
        // Using the repository method findByRoleAndNic to verify that an "Adult" account exists.
        List<Patient> existingPatients = patientRepository.findByRoleAndNic("Adult", patient.getNic());
        if (existingPatients == null || existingPatients.isEmpty()) {
            throw new RuntimeException("No account found for this NIC");
        }
        patient.setPassword(passwordEncoder.encode(patient.getPassword())); // Encrypt password
        return patientRepository.save(patient);
    }

    public List<String> login(long mediId, String password) {
        Optional<Patient> patientOpt = patientRepository.findByMediId(mediId);
        if (patientOpt.isPresent()) {
            Patient patient = patientOpt.get();
            if (passwordEncoder.matches(password, patient.getPassword())) {
                String token = JwtUtil.generateToken(String.valueOf(mediId));
                return List.of(token);
            }
        }
        return Collections.emptyList();
    }

    public Optional<Patient> getPatientByMediId(Long mediId) {
        return patientRepository.findByMediId(mediId);
    }

    public Patient savePatient(Patient patient) {
        return patientRepository.save(patient);
    }

    public Patient updatePatient(long id, Patient updatedPatient) {
        return patientRepository.findById(id).map(patient -> {
            patient.setFirstName(updatedPatient.getFirstName());
            patient.setLastName(updatedPatient.getLastName());
            patient.setEmail(updatedPatient.getEmail());
            patient.setNic(updatedPatient.getNic());
            patient.setContactNumber(updatedPatient.getContactNumber());
            patient.setPassword(updatedPatient.getPassword());
            patient.setRole(updatedPatient.getRole());
            patient.setBirthday(updatedPatient.getBirthday());
            patient.setAddress(updatedPatient.getAddress());
            patient.setBloodGroup(updatedPatient.getBloodGroup());
            patient.setGender(updatedPatient.getGender());
            patient.setHeight(updatedPatient.getHeight());
            patient.setWeight(updatedPatient.getWeight());
            patient.setAllergy(updatedPatient.getAllergy());
            patient.setProfilePicture(updatedPatient.getProfilePicture());
            return patientRepository.save(patient);
        }).orElse(null);
    }

    public void deletePatient(long id) {
        patientRepository.deleteById(id);
    }

    public String verifyUserAndGetEmail(String nic, long mediId) {
        // Fetch the patient by NIC and MediID
        Optional<Patient> patientOpt = patientRepository.findByNicAndMediId(nic, mediId);
        if (patientOpt.isPresent()) {
            return patientOpt.get().getEmail();
        }
        return "User email not found";
    }

    // Update password method with encryption for the new password.
    public boolean updatePassword(Long mediId, String newPassword) {
        Optional<Patient> patientOptional = patientRepository.findByMediId(mediId);
        if (patientOptional.isPresent()) {
            Patient patient = patientOptional.get();
            patient.setPassword(passwordEncoder.encode(newPassword)); // Encrypt new password
            patientRepository.save(patient);
            return true;
        }
        return false;
    }

    public boolean verifyOldPassword(Long mediId, String oldPassword) {
        Optional<Patient> patientOptional = patientRepository.findByMediId(mediId);
        if (patientOptional.isEmpty()) {
            return false;   // Invalid MediID
        }
        Patient patient = patientOptional.get();
        return passwordEncoder.matches(oldPassword, patient.getPassword());
    }

    public boolean changePassword(Long mediId, String oldPassword, String newPassword) {
        Optional<Patient> patientOptional = patientRepository.findByMediId(mediId);
        if (patientOptional.isEmpty()) {
            return false;
        }
        Patient patient = patientOptional.get();

        // Verify the existing password before updating
        if (!verifyOldPassword(mediId, oldPassword)) {
            return false;
        }

        // Encrypt the new password and update
        patient.setPassword(passwordEncoder.encode(newPassword));
        patientRepository.save(patient);
        return true;
    }

    public Optional<Patient> getUserByMediId(long mediId) {
        return patientRepository.findByMediId(mediId);
    }
}
