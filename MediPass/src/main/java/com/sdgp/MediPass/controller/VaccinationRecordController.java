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
import org.springframework.web.server.ResponseStatusException;

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

    @ApiOperation(value = "Storing vaccination records")
    @PostMapping("/add-V-records")
    public ResponseEntity<?> addVaccinationRecords(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestBody VaccinationRecords vaccinationRecords) {

        try {
            long mediId = extractMediId(token);
            VaccinationRecords savedRecords = vaccinationRecordService.addVaccinationRecords(mediId, vaccinationRecords);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedRecords);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @ApiOperation(value = "Retrieving vaccination records")
    @GetMapping("/get-V-records")
    public ResponseEntity<?> getAllVaccinationRecords(@RequestHeader(value = "Authorization", required = false) String token) {
        try {
            long mediId = extractMediId(token);
            return ResponseEntity.ok(vaccinationRecordService.getAllVaccinationRecords(mediId).toString());
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
