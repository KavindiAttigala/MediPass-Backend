package com.sdgp.MediPass.controller;

import com.sdgp.MediPass.enums.ReportType;
import com.sdgp.MediPass.model.MedicalReports;
import com.sdgp.MediPass.model.Patient;
import com.sdgp.MediPass.service.MedicalReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
@Api(value = "Medical Reports", description = "Managing medical reports of the patient")
public class MedicalReportController {

    @Autowired
    private MedicalReportService medicalReportService;

    @ApiOperation(value = "Retrieving all medical reports")
    @GetMapping
    public List<MedicalReports> getAllReports() {
        return medicalReportService.getAllReports();
    }

    @ApiOperation(value = "Retrieving medical reports by mediId")
    @GetMapping("/{id}")
    public ResponseEntity<MedicalReports> getReportById(@PathVariable Long id) {
        return medicalReportService.getReportById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @ApiOperation(value = "Storing medical reports")
    @PostMapping("/upload")
    public ResponseEntity<MedicalReports> uploadReport(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("patientId") Long patientId) {

        try {
            Patient patient = new Patient(); // Retrieve patient from DB if needed
            patient.setMediId(patientId);

            MedicalReports savedReport = medicalReportService.saveReportWithFile(file, title, description, patient);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedReport);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/patient")
    public List<MedicalReports> getReportsByPatient(@RequestParam Patient patient) {
        return medicalReportService.getReportsByPatientId(patient.getMediId());
    }

    @GetMapping("/type/{reportType}")
    public List<MedicalReports> getReportsByType(@PathVariable ReportType reportType) {
        return medicalReportService.getReportsByType(reportType);
    }

    @GetMapping("/patient/{patientId}/type/{reportType}")
    public List<MedicalReports> getReportsByPatientAndType(@PathVariable Long patientId, @PathVariable ReportType reportType) {
        return medicalReportService.getReportsByPatientAndType(patientId, reportType);
    }



}

