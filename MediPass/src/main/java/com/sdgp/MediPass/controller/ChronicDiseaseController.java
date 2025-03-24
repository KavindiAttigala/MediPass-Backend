package com.sdgp.MediPass.controller;

import com.sdgp.MediPass.model.ChronicDisease;
import com.sdgp.MediPass.service.ChronicDiseaseService;
import com.sdgp.MediPass.util.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/medipass/chronic-disease")
@Api(value = "Chronic Disease Records", description = "Managing a record of chronic diseases and medication of the user")
public class ChronicDiseaseController {
    @Autowired
    private ChronicDiseaseService chronicService;

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


    @ApiOperation(value = "Storing chronic disease records in DB")
    @PostMapping("/add-disease")
    public ResponseEntity<?> addDisease(@RequestHeader(value = "Authorization", required = false) String token,@RequestParam long mediId, @RequestParam String diseaseName){
        ResponseEntity<String> validateToken = validateToken(token);
        if(!validateToken.getStatusCode().is2xxSuccessful()){
            return validateToken;
        }
        try{
            ChronicDisease chronic = chronicService.addDisease(mediId,diseaseName);
            return ResponseEntity.ok(chronic);
        }catch(IOException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @ApiOperation(value = "Storing the medications of chronic diseases")
    @PostMapping("/add-medication")
    public ResponseEntity<?> addMedication(@RequestHeader(value = "Authorization", required = false) String token,@RequestParam long mediId, @RequestParam String medication, @RequestParam int dosage, @RequestParam LocalDate start, @RequestParam LocalDate end){
        ResponseEntity<String> validateToken = validateToken(token);
        if(!validateToken.getStatusCode().is2xxSuccessful()){
            return validateToken;
        }try{
            ChronicDisease chronicDisease = chronicService.addMedication(mediId, medication, dosage, start, end);
            return ResponseEntity.ok(chronicDisease);
        }catch(IOException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @ApiOperation(value = "Retrieving chronic disease records from DB")
    @GetMapping("/get-disease")
    public ResponseEntity<?> getDisease(@RequestHeader(value = "Authorization", required = false) String token,@RequestParam long mediId) {
        ResponseEntity<String> validateToken = validateToken(token);
        if(!validateToken.getStatusCode().is2xxSuccessful()){
            return validateToken;
        }try {
            List<ChronicDisease> diseases = chronicService.getDisease(mediId);
            return ResponseEntity.ok(diseases);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @ApiOperation(value = "Retrieve all medications for a specific chronic disease")
    @GetMapping("/get-medications")
    public ResponseEntity<?> getMedications(@RequestHeader(value = "Authorization", required = false) String token,@RequestParam long mediId, @RequestParam String diseaseName) {
        ResponseEntity<String> validateToken = validateToken(token);
        if(!validateToken.getStatusCode().is2xxSuccessful()){
            return validateToken;
        }try {
            List<String> medications = chronicService.getMedications(mediId, diseaseName);
            return ResponseEntity.ok(medications);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
