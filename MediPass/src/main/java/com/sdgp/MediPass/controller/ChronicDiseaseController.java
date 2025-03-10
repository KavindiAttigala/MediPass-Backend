package com.sdgp.MediPass.controller;

import com.sdgp.MediPass.model.ChronicDisease;
import com.sdgp.MediPass.service.ChronicDiseaseService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/chronic-disease")
@Api(value = "Chronic Disease Records", description = "Managing a record of chronic diseases and medication of the user")
public class ChronicDiseaseController {
    @Autowired
    private ChronicDiseaseService chronicService;

    @PostMapping("/add-disease")
    public ResponseEntity<?> addDisease(@RequestParam long mediId, @RequestParam String diseaseName){
        try{
            ChronicDisease chronic = chronicService.addDisease(mediId,diseaseName);
            return ResponseEntity.ok(chronic);
        }catch(IOException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/add-medication")
    public ResponseEntity<?> addMedication(@RequestParam long mediId, @RequestParam String medication, @RequestParam int dosage, @RequestParam char start, @RequestParam char end){
        try{
            ChronicDisease chronicDisease = chronicService.addMedication(mediId, medication, dosage, start, end);
            return ResponseEntity.ok(chronicDisease);
        }catch(IOException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/get-disease")
    public ResponseEntity<?> getDisease(@RequestParam long mediId) {
        try {
            List<ChronicDisease> diseases = chronicService.getDisease(mediId);
            return ResponseEntity.ok(diseases);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
