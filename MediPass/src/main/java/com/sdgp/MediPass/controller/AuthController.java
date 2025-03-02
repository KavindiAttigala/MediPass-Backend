package com.sdgp.MediPass.controller;

import com.sdgp.MediPass.model.Patient;
import com.sdgp.MediPass.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private PatientService patientService;

    @PostMapping("/register/adult")
    public ResponseEntity<?> registerAdult(@RequestBody Patient patient) {
        Patient savedPatient = patientService.registerPatientAdult(patient);
        return ResponseEntity.ok(savedPatient);
    }

    @PostMapping("/register/child")
    public ResponseEntity<?> registerChild(@RequestBody Patient patient) {
        Patient savedPatient = patientService.registerPatientChild(patient);
        return ResponseEntity.ok(savedPatient);
    }


}


