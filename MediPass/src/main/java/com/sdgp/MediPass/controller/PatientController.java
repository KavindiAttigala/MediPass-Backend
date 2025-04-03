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
import java.util.Optional;

@RestController
@RequestMapping("/medipass/patient")
@Api(value = "Patient Data", description = "Creating profiles for new patients and updating existing patient profiles")
public class PatientController {

    @Autowired
    private PatientService patientService;
    @Autowired
    private JwtUtil jwtUtil;

    // Utility method to validate token and extract mediId
    private String validateToken(String token) throws Exception {
        if (token == null || !token.startsWith("Bearer ")) {
            throw new Exception("Invalid token");
        }
        String actualToken = token.substring(7);
        String mediId = jwtUtil.extractMediId(actualToken);
        if (mediId == null) {
            throw new Exception("Invalid or expired token");
        }
        return mediId;
    }

    @ApiOperation(value = "Retrieve Patient Profile", notes = "Get patient details by MediID")
    @GetMapping("/profile")
    public ResponseEntity<?> getPatientProfile(@RequestHeader("Authorization") String token) {
        try {
            String actualToken = token.substring(7);
            String mediId = jwtUtil.extractMediId(actualToken);
            Optional<Patient> patient = patientService.getPatientByMediId(Long.valueOf(mediId));
            if (patient.isPresent()) {
                return ResponseEntity.ok(patient.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @ApiOperation(value = "Create Patient Profile", notes = "Save new patient details")
    @PostMapping
    public ResponseEntity<Patient> createPatient(@RequestBody Patient patient) {
        Patient savedPatient = patientService.savePatient(patient);
        return ResponseEntity.ok(savedPatient);
    }

    @ApiOperation(value = "Update Patient Profile", notes = "Update patient details based on MediID")
    @PutMapping("/{id}")
    public ResponseEntity<Patient> updatePatient(@PathVariable long id,
                                                 @RequestBody Patient updatedPatient,
                                                 @RequestHeader("Authorization") String token) {
        try {
            String extractedMediId = validateToken(token);
            // Ensure that the logged-in patient (from token) is updating their own profile
            if (!extractedMediId.equals(String.valueOf(id))) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }
            Patient patient = patientService.updatePatient(id, updatedPatient);
            return patient != null ? ResponseEntity.ok(patient) : ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable long id,
                                              @RequestHeader("Authorization") String token) {
        try {
            String extractedMediId = validateToken(token);
            if (!extractedMediId.equals(String.valueOf(id))) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }
            patientService.deletePatient(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @ApiOperation(value = "Change password by verifying existing password")
    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestHeader("Authorization") String token,
                                                 @RequestParam String oldPassword,
                                                 @RequestParam String newPassword) {
        try {
            String actualToken = token.substring(7);
            String mediIdStr = jwtUtil.extractMediId(actualToken);
            long mediId = Long.parseLong(mediIdStr);
            boolean change = patientService.changePassword(mediId, oldPassword, newPassword);
            if (change) {
                return ResponseEntity.ok("Password changed");
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        }
    }
}
