package com.sdgp.MediPass.controller;

import com.sdgp.MediPass.model.VaccinationRecords;
import com.sdgp.MediPass.service.VaccinationRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medipass/vaccinations")
@Api(value = "Vaccination Records", description = "Managing vaccination records of the patient")
public class VaccinationRecordController {
    @Autowired
    private VaccinationRecordService vaccinationRecordService;

    @ApiOperation(value = "Storing vaccination records")
    @PostMapping("/add-V-records/mediId/{mediId}")
    public ResponseEntity<VaccinationRecords> addVaccinationRecords(@RequestParam long mediId, @RequestBody VaccinationRecords vaccinationRecords){
        VaccinationRecords savedRecords= vaccinationRecordService.addVaccinationRecords(mediId,vaccinationRecords);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRecords);
    }

    @ApiOperation(value = "Retrieving vaccination records")
    @GetMapping("/get-V-records")
    public ResponseEntity<List<VaccinationRecords>> getAllVaccinationRecords(){
        return ResponseEntity.ok(vaccinationRecordService.getAllVaccinationRecords());
    }

}
