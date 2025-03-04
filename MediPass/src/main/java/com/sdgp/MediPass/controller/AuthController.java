package com.sdgp.MediPass.controller;

import com.sdgp.MediPass.DTO.LoginRequest;
import com.sdgp.MediPass.model.Patient;
import com.sdgp.MediPass.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private PatientService patientService;
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register/adult")
    public ResponseEntity<?> registerAdult(@RequestBody Patient patient) {
        patient.setRole("Adult");
        Patient savedPatient = patientService.registerPatientAdult(patient);
        return ResponseEntity.ok(savedPatient);
    }

    @PostMapping("/register/child")
    public ResponseEntity<?> registerChild(@RequestBody Patient patient) {
        patient.setRole("Child");
        Patient savedPatient = patientService.registerPatientChild(patient);
        return ResponseEntity.ok(savedPatient);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        List<Patient> patientList = patientService.getUserByMediId(request.getMediId());
        if (!patientList.isEmpty()) {
            Patient patient = patientList.get(0);
            if (passwordEncoder.matches(request.getPassword(), patient.getPassword())) {
                return ResponseEntity.ok(patient);
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }


}


