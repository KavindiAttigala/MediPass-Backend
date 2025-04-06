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
import org.springframework.web.server.ResponseStatusException;

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


    @ApiOperation(value = "Storing chronic disease records in DB")
    @PostMapping("/add-disease")
    public ResponseEntity<?> addDisease(@RequestHeader(value = "Authorization", required = false) String token,
                                        @RequestParam String diseaseName,
                                        @RequestParam String medication, @RequestParam int dosage,
                                        @RequestParam LocalDate start, @RequestParam LocalDate end){
        long mediId = extractMediId(token);
        try {
            ChronicDisease chronic = chronicService.addDiseaseRecords(diseaseName, mediId, medication, dosage, start,end);
            return ResponseEntity.ok(chronic);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @ApiOperation(value = "Retrieving chronic disease records from DB")
    @GetMapping("/get-disease")
    public ResponseEntity<?> getDisease(@RequestHeader(value = "Authorization", required = false) String token) {
        long mediId = extractMediId(token);
        try {
            List<ChronicDisease> diseases = chronicService.getDisease(mediId);
            return ResponseEntity.ok(diseases);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
