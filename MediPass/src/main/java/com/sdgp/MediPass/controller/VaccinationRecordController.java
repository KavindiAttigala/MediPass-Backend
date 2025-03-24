package com.sdgp.MediPass.controller;

import com.sdgp.MediPass.model.BloodDonationRecords;
import com.sdgp.MediPass.model.VaccinationRecords;
import com.sdgp.MediPass.service.VaccinationRecordService;
import com.sdgp.MediPass.util.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/medipass/vaccinations")
@Api(value = "Vaccination Records", description = "Managing vaccination records of the patient")
public class VaccinationRecordController {
    @Autowired
    private VaccinationRecordService vaccinationRecordService;
    @Autowired
    private JwtUtil jwtUtil;


    //validate the extracted token
    private ResponseEntity<String> validateToken(String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid token");
        }
        String actualToken = token.substring(7);
        String mediId = jwtUtil.extractMediId(actualToken);
        if (mediId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        }
        return ResponseEntity.ok(mediId);  // Return extracted MediID if valid
    }

    @ApiOperation(value = "Storing vaccination records")
    @PostMapping("/add-V-records/mediId/{mediId}")
    public ResponseEntity<? extends Object> addVaccinationRecords(@RequestHeader(value = "Authorization", required = false) String token, @PathVariable long mediId, @RequestBody VaccinationRecords vaccinationRecords){
        ResponseEntity<String> validateToken = validateToken(token);
        if(!validateToken.getStatusCode().is2xxSuccessful()){
            return validateToken;
        }try{
            VaccinationRecords savedRecords= vaccinationRecordService.addVaccinationRecords(mediId,vaccinationRecords);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedRecords);    }
        catch(RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @ApiOperation(value = "Retrieving vaccination records")
    @GetMapping("/get-V-records/mediId/{mediId}")
    public ResponseEntity<String> getAllVaccinationRecords(@RequestHeader(value = "Authorization", required = false) String token, @PathVariable long mediId){
        ResponseEntity<String> validateToken = validateToken(token);
        if(!validateToken.getStatusCode().is2xxSuccessful()){
            return validateToken;
        }try{
            return ResponseEntity.ok(vaccinationRecordService.getAllVaccinationRecords(mediId).toString());
        }
        catch(RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
