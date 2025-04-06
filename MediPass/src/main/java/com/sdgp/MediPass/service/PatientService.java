package com.sdgp.MediPass.service;

import com.sdgp.MediPass.model.Patient;
import com.sdgp.MediPass.repository.PatientRepository;
import com.sdgp.MediPass.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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

    @Autowired
    private JavaMailSender mailSender;

    public Patient registerPatientAdult(Patient patient) {
        // Assuming NIC is the unique identifier. Adjust if needed.
        List<Patient> existingPatients = patientRepository.findByNic(patient.getNic());
        if (existingPatients != null && !existingPatients.isEmpty()) {
            throw new RuntimeException("Account already exists for this NIC");
        }
        patient.setPassword(passwordEncoder.encode(patient.getPassword())); // Encrypt password

        Patient savedPatient= patientRepository.save(patient);

        // Send email with the generated MediId to login
        sendEmail(savedPatient.getEmail(), savedPatient.getMediId(), savedPatient.getFirstName(), savedPatient.getLastName());
        return savedPatient;
    }

    public Patient registerPatientChild(Patient patient) {
        // Using the repository method findByRoleAndNic to verify that an "Adult" account exists.
        List<Patient> existingPatients = patientRepository.findByRoleAndNic("Adult", patient.getNic());
        if (existingPatients == null || existingPatients.isEmpty()) {
            throw new RuntimeException("No account found for this NIC");
        }
        patient.setPassword(passwordEncoder.encode(patient.getPassword())); // Encrypt password
        Patient savedPatient= patientRepository.save(patient);

        // Send email with the generated MediId to login
        sendEmail(savedPatient.getEmail(), savedPatient.getMediId(), savedPatient.getFirstName(), savedPatient.getLastName());
        return savedPatient;
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
            if (updatedPatient.getFirstName() != null) {
                patient.setFirstName(updatedPatient.getFirstName());
            }
            if (updatedPatient.getLastName() != null) {
                patient.setLastName(updatedPatient.getLastName());
            }
            if (updatedPatient.getEmail() != null) {
                patient.setEmail(updatedPatient.getEmail());
            }
            if (updatedPatient.getNic() != null) {
                patient.setNic(updatedPatient.getNic());
            }
            if (updatedPatient.getContactNumber() != null) {
                patient.setContactNumber(updatedPatient.getContactNumber());
            }
            if (updatedPatient.getPassword() != null) {
                patient.setPassword(updatedPatient.getPassword());
            }
            if (updatedPatient.getRole() != null) {
                patient.setRole(updatedPatient.getRole());
            }
            if (updatedPatient.getBirthday() != null) {
                patient.setBirthday(updatedPatient.getBirthday());
            }
            if (updatedPatient.getAddress() != null) {
                patient.setAddress(updatedPatient.getAddress());
            }
            if (updatedPatient.getBloodGroup() != null) {
                patient.setBloodGroup(updatedPatient.getBloodGroup());
            }
            if (updatedPatient.getGender() != null) {
                patient.setGender(updatedPatient.getGender());
            }
            // For numeric fields, update them regardless (or add additional checks if needed)
            patient.setHeight(updatedPatient.getHeight());
            patient.setWeight(updatedPatient.getWeight());
            if (updatedPatient.getAllergy() != null) {
                patient.setAllergy(updatedPatient.getAllergy());
            }
            if (updatedPatient.getProfilePicture() != null) {
                patient.setProfilePicture(updatedPatient.getProfilePicture());
            }
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

    private void sendEmail(String email, long mediId, String firstName, String lastName) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email);
        msg.setSubject("Registration to MediPass is successful");
        msg.setText(" Welcome "+ firstName + " " + lastName
                + " You have successfully completed the sign up process. To complete setting up your MediPass account use the following MediID. "
                + "We advice you to not share your MediID with anyone. \n\n"
                + "Your MediID is: "+mediId+"\n\nMediPass \nSmart Records Smarter Care.");
        mailSender.send(msg);
    }

    public Optional<Patient> getUserByMediId(long mediId) {
        return patientRepository.findByMediId(mediId);
    }
}
