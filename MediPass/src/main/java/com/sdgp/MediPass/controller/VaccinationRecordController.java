package com.sdgp.MediPass.controller;

import com.sdgp.MediPass.model.VaccinationRecords;
import com.sdgp.MediPass.service.VaccinationRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vaccinations")
public class VaccinationRecordController {
    @Autowired
    private VaccinationRecordService vaccinationRecordService;

    @PostMapping
    public ResponseEntity<VaccinationRecords> addVaccinationRecords(@RequestBody VaccinationRecords bloodDonation){
        VaccinationRecords savedRecords= vaccinationRecordService.addVaccinationRecords(bloodDonation);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRecords);
    }

    @GetMapping
    public ResponseEntity<List<VaccinationRecords>> getAllVaccinationRecords(){
        return ResponseEntity.ok(vaccinationRecordService.getAllVaccinationRecords());
    }

}
