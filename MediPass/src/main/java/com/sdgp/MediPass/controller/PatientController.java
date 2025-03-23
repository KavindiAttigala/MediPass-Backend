package com.sdgp.MediPass.controller;

import com.sdgp.MediPass.model.Patient;
import com.sdgp.MediPass.service.PatientService;
import com.sdgp.MediPass.util.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/patient")
@Api(value = "Patient Data", description = "Creating profiles for new patients and updating existing patient profiles")
public class PatientController {

    @Autowired
    private PatientService patientService;
    @Autowired
    private JwtUtil jwtUtil;

    @ApiOperation(value = "Retrieve Patient Profile", notes = "Get patient details by MediID")
    @GetMapping("/{mediId}")
    public ResponseEntity<Patient> getPatientById(@PathVariable long mediId) {
        Optional<Patient> patients = patientService.getPatientByMediId(mediId);
        if (!patients.isEmpty()) {
            return ResponseEntity.ok(patients.get()); // Return the first patient
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getPatientProfile(@RequestHeader("Authorization") String token) {
        try {
            // Extract token from "Bearer <token>"
            String actualToken = token.substring(7);
            String mediId = jwtUtil.extractMediId(actualToken);

            Optional<Patient> patient = patientService.getPatientByMediId(Long.valueOf(mediId));

            if (patient.isPresent()) {
                return ResponseEntity.ok(patient.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Ensures consistent return type
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // Return 401 without a body
        }
    }



    @ApiOperation(value = "Create Patient Profile", notes = "Save new patient details")
    @PostMapping
    public ResponseEntity<Patient> createPatient(@RequestBody Patient patient) {
        Patient savedPatient = patientService.savePatient(patient);
        return ResponseEntity.ok(savedPatient);
    }

    @ApiOperation(value= "Update Patient Profile", notes= "Update patient details based on MediID")
    @PutMapping("/{id}")
    public ResponseEntity<Patient> updatePatient(@PathVariable long id, @RequestBody Patient updatedPatient) {
        Patient patient = patientService.updatePatient(id, updatedPatient);
        return patient != null ? ResponseEntity.ok(patient) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable long id) {
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "Change password by verifying existing password")
    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestParam long mediId, @RequestParam String oldPassword, @RequestParam String newPassword) {
        boolean change = patientService.changePassword(mediId, oldPassword, newPassword);
        if (change) {
            return ResponseEntity.ok("Password changed");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
