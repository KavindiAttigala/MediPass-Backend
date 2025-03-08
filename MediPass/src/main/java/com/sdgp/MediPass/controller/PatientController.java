package com.sdgp.MediPass.controller;

import com.sdgp.MediPass.model.Patient;
import com.sdgp.MediPass.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/patient")
public class PatientController {
    @Autowired
    private PatientService patientService;

    @PutMapping("/{mediId}")
    public ResponseEntity<?> updateUserProfile(@PathVariable Long mediId, @RequestBody Patient updatedPatient) {
        List<Patient> patientsList = patientService.getUserByMediId(mediId);
        if (!patientsList.isEmpty()) {
            Patient patient = patientsList.get(0); // Retrieve the first patient from the list
            patient.setFirstName(updatedPatient.getFirstName());
            patient.setLastName(updatedPatient.getLastName());
            patient.setContactNumber(updatedPatient.getContactNumber());
            patientService.updatePatient(patient); // Ensure you have an update method in your service
            return ResponseEntity.ok(patient);
        }
        return ResponseEntity.notFound().build();
    }

}
