package com.sdgp.MediPass.controller;

import com.sdgp.MediPass.model.BloodDonationRecords;
import com.sdgp.MediPass.service.BloodDonationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.persistence.GeneratedValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medipass/blood-donations")
@Api(value="Blood Donation Records", description="Storing blood donation records of the patient")
public class BloodDonationController {

    @Autowired
    private BloodDonationService bloodDonationService;

    @ApiOperation(value = "Add a new blood donation record", notes = "Submit patient blood donation records to store in the database")
    @PostMapping("/add-B-records/mediId/{mediId}")
    public ResponseEntity<BloodDonationRecords> addBloodDonation(@PathVariable long mediId, @RequestBody BloodDonationRecords bloodDonation){
        BloodDonationRecords savedDonation = bloodDonationService.saveBDRecords(mediId,bloodDonation);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDonation);
    }

    @ApiOperation(value = "Get all blood donation records", notes = "Retrieve all stored blood donation records")
    @GetMapping("/get-B-records/mediId/{mediId}")
    public ResponseEntity<List<BloodDonationRecords>> getAllBloodDonations(@PathVariable long mediId){
        return ResponseEntity.ok(bloodDonationService.getAllDonations(mediId));
    }
}
