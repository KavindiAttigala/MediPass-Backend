package com.sdgp.MediPass.controller;

import com.sdgp.MediPass.model.BloodDonationRecords;
import com.sdgp.MediPass.service.BloodDonationService;
import jakarta.persistence.GeneratedValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/blood-donations")
public class BloodDonationController {

    @Autowired
    private BloodDonationService bloodDonationService;

    @PostMapping
    public ResponseEntity<BloodDonationRecords> addBloodDonation(@RequestBody BloodDonationRecords bloodDonation){
        BloodDonationRecords savedDonation = bloodDonationService.saveBDRecords(bloodDonation);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDonation);
    }

    @GetMapping
    public ResponseEntity<List<BloodDonationRecords>> getAllBloodDonations(){
        return ResponseEntity.ok(bloodDonationService.getAllDonations());
    }
}
