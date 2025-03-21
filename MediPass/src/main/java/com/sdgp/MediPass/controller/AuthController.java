package com.sdgp.MediPass.controller;

import com.sdgp.MediPass.DTO.LoginRequest;
import com.sdgp.MediPass.model.Patient;
import com.sdgp.MediPass.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
        List<String> response = patientService.login(request.getMediId(), request.getPassword());

        if (!response.isEmpty()) {
            return ResponseEntity.ok(Map.of(
                    "token", response.get(0),
                    "mediId", request.getMediId(),
                    "message", "Login successful"
            ));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }


}


