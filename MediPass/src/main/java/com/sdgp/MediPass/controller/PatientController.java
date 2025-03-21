package com.sdgp.MediPass.controller;

import com.sdgp.MediPass.model.Patient;
import com.sdgp.MediPass.service.PatientService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/patient")
@Api(value = "Patient Data", description = "Creating profiles for new patients and updating existing patient profiles")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @ApiOperation(value = "Update Patient Profile", notes = "Update patient details based on MediID")
    @PutMapping("/{mediId}")
    public ResponseEntity<?> updateUserProfile(@PathVariable Long mediId, @RequestBody Patient updatedPatient) {
        Optional<Patient> patientOpt = patientService.getUserByMediId(mediId);
        if (patientOpt.isPresent()) {
            Patient patient = patientOpt.get();
            patient.setFirstName(updatedPatient.getFirstName());
            patient.setLastName(updatedPatient.getLastName());
            patient.setContactNumber(updatedPatient.getContactNumber());
            patientService.updatePatient(patient); // Update the patient profile in the database
            return ResponseEntity.ok(patient);
        }
        return ResponseEntity.notFound().build();
    }

    @ApiOperation(value = "Change password by verifying existing password")
    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestParam long mediId,
                                                 @RequestParam String oldPassword,
                                                 @RequestParam String newPassword) {
        boolean change = patientService.changePassword(mediId, oldPassword, newPassword);
        if (change) {
            return ResponseEntity.ok("Password changed");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
