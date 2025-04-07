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
import org.springframework.web.server.ResponseStatusException;

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
    private long extractMediId(String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing or invalid token");
        }
        String actualToken = token.substring(7);
        String mediId = jwtUtil.extractMediId(actualToken);
        if (mediId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid or expired token");
        }
        return Long.parseLong(mediId);
    }
//    private ResponseEntity<String> validateToken(String token) {
//        return extractMediId(token, jwtUtil);
//    }

    @ApiOperation(value = "Add a new blood donation record", notes = "Submit patient blood donation records to store in the database")
    @PostMapping("/add-BD-records")
    public ResponseEntity<?> addBloodDonation(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestBody BloodDonationRecords bloodDonation) {

        try {
            long mediId = extractMediId(token);
            BloodDonationRecords savedDonation = bloodDonationService.saveBDRecords(mediId, bloodDonation);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedDonation);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @ApiOperation(value = "Get all blood donation records", notes = "Retrieve all stored blood donation records")
    @GetMapping("/get-BD-records")
    public ResponseEntity<?> getAllBloodDonations(@RequestHeader(value = "Authorization", required = false) String token) {
        try {
            long mediId = extractMediId(token);
            List<BloodDonationRecords> getRecords = bloodDonationService.getAllDonations(mediId);
            return ResponseEntity.ok(getRecords);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
