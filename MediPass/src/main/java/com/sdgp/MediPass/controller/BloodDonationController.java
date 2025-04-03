package com.sdgp.MediPass.controller;

import com.sdgp.MediPass.model.BloodDonationRecords;
import com.sdgp.MediPass.service.BloodDonationService;
import com.sdgp.MediPass.util.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.persistence.GeneratedValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.sdgp.MediPass.controller.OTPController.extractMediId;

@RestController
@RequestMapping("/medipass/blood-donations")
@Api(value="Blood Donation Records", description="Storing blood donation records of the patient")
public class BloodDonationController {

    @Autowired
    private BloodDonationService bloodDonationService;

    @Autowired
    private JwtUtil jwtUtil;


    //validate the extracted token
    private ResponseEntity<String> validateToken(String token) {
        return extractMediId(token, jwtUtil);
    }

    @ApiOperation(value = "Add a new blood donation record", notes = "Submit patient blood donation records to store in the database")
    @PostMapping("/add-B-records/mediId/{mediId}")
    public ResponseEntity<String> addBloodDonation(@RequestHeader(value = "Authorization", required = false) String token,@PathVariable long mediId, @RequestBody BloodDonationRecords bloodDonation){
        ResponseEntity<String> validateToken = validateToken(token);
        if(!validateToken.getStatusCode().is2xxSuccessful()){
            return validateToken;
        }try{
            BloodDonationRecords savedDonation = bloodDonationService.saveBDRecords(mediId,bloodDonation);
            return ResponseEntity.status(HttpStatus.CREATED).body(String.valueOf(savedDonation));        }
        catch(RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        
    }

    @ApiOperation(value = "Get all blood donation records", notes = "Retrieve all stored blood donation records")
    @GetMapping("/get-B-records/mediId/{mediId}")
    public ResponseEntity<String> getAllBloodDonations(@RequestHeader(value = "Authorization", required = false) String token,@PathVariable long mediId){
        ResponseEntity<String> validateToken = validateToken(token);
        if(!validateToken.getStatusCode().is2xxSuccessful()){
            return validateToken;
        }try{
            return ResponseEntity.ok(bloodDonationService.getAllDonations(mediId).toString());
        }
        catch(RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }


    }
}
