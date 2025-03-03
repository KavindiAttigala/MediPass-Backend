package com.sdgp.MediPass.controller;

import com.sdgp.MediPass.model.ChronicDisease;
import com.sdgp.MediPass.service.ChronicDiseaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/chronic-disease")
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

        /*
        catch (IllegalArgumentException e) {        //catch cases where the provided mediId does not match any patient in the database
            return ResponseEntity.badRequest().body(null);
        } catch (IOException e) {       //catch potential I/O-related issues that might occur when interacting with the database or other system resources
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
        */

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

}
