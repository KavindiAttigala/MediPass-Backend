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

import java.util.List;

import static com.sdgp.MediPass.controller.OTPController.extractMediId;

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
        return extractMediId(token, jwtUtil);
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

    @GetMapping("/get-V-records/mediId/{mediId}")
    public ResponseEntity<List<VaccinationRecords>> getAllVaccinationRecords(
            @RequestHeader(value = "Authorization", required = false) String token,
            @PathVariable long mediId
    ) {
        ResponseEntity<String> validateToken = validateToken(token);
        if (!validateToken.getStatusCode().is2xxSuccessful()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        try {
            List<VaccinationRecords> records = vaccinationRecordService.getAllVaccinationRecords(mediId);
            return ResponseEntity.ok(records);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

}