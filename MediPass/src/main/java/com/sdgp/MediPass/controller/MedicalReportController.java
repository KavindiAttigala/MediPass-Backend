package com.sdgp.MediPass.controller;

import com.sdgp.MediPass.enums.ReportType;
import com.sdgp.MediPass.model.BloodDonationRecords;
import com.sdgp.MediPass.model.MedicalReports;
import com.sdgp.MediPass.model.Patient;
import com.sdgp.MediPass.service.MedicalReportService;
import com.sdgp.MediPass.util.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.sdgp.MediPass.controller.OTPController.extractMediId;

@RestController
@RequestMapping("/medipass/api/reports")
@Api(value = "Medical Reports", description = "Managing medical reports of the patient")
public class MedicalReportController {

    @Autowired
    private MedicalReportService medicalReportService;

    @Autowired
    private JwtUtil jwtUtil;


    //validate the extracted token
    private ResponseEntity<String> validateToken(String token) {
        return extractMediId(token, jwtUtil);
    }


    @ApiOperation(value = "Retrieving all medical reports")
    @GetMapping
    public ResponseEntity<String> getAllReports(@RequestHeader(value = "Authorization", required = false) String token) {
        ResponseEntity<String> validateToken = validateToken(token);
        if(!validateToken.getStatusCode().is2xxSuccessful()){
            return validateToken;
        }try{
            return (ResponseEntity<String>) medicalReportService.getAllReports();}
        catch(RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @ApiOperation(value = "Retrieving medical reports by mediId")
    @GetMapping("/{id}")
    public ResponseEntity<? extends Object> getReportById(@RequestHeader(value = "Authorization", required = false) String token, @PathVariable Long id) {
        ResponseEntity<String> validateToken = validateToken(token);
        if(!validateToken.getStatusCode().is2xxSuccessful()){
            return validateToken;
        }try{
            return medicalReportService.getReportById(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build()); }
        catch(RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @ApiOperation(value = "Storing medical reports")
    @PostMapping("/upload")
    public ResponseEntity<? extends Object> uploadReport(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("patientId") Long patientId) {

        ResponseEntity<String> validateToken = validateToken(token);
        if(!validateToken.getStatusCode().is2xxSuccessful()){
            return validateToken;
        }
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
    public List<MedicalReports> getReportsByPatient(@RequestHeader(value = "Authorization", required = false) String token,@RequestParam Patient patient) {
        ResponseEntity<String> validateToken = validateToken(token);
        if(!validateToken.getStatusCode().is2xxSuccessful()){
            return (List<MedicalReports>) validateToken;
        }try{
            return medicalReportService.getReportsByPatientId(patient.getMediId());}

        catch(RuntimeException e){
            return (List<MedicalReports>) ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/type/{reportType}")
    public List<MedicalReports> getReportsByType(@RequestHeader(value = "Authorization", required = false) String token,@PathVariable ReportType reportType) {
        ResponseEntity<String> validateToken = validateToken(token);
        if(!validateToken.getStatusCode().is2xxSuccessful()){
            return (List<MedicalReports>) validateToken;
        }try{
            return medicalReportService.getReportsByType(reportType);

        }
        catch(RuntimeException e){
            return (List<MedicalReports>) ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/patient/{patientId}/type/{reportType}")
    public List<MedicalReports> getReportsByPatientAndType(@RequestHeader(value = "Authorization", required = false) String token,@PathVariable Long patientId, @PathVariable ReportType reportType) {
        ResponseEntity<String> validateToken = validateToken(token);
        if(!validateToken.getStatusCode().is2xxSuccessful()){
            return (List<MedicalReports>) validateToken;
        }try{
            return medicalReportService.getReportsByPatientAndType(patientId, reportType);
        }
        catch(RuntimeException e){
            return (List<MedicalReports>) ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/xRay/patient/{patientId}")
    public List<MedicalReports> getXRayReportsByPatientId(@RequestHeader(value = "Authorization", required = false) String token,@PathVariable Long patientId) {
        ResponseEntity<String> validateToken = validateToken(token);
        if(!validateToken.getStatusCode().is2xxSuccessful()){
            return (List<MedicalReports>) validateToken;
        }try{
            return medicalReportService.getReportsByPatientAndType(patientId, ReportType.valueOf("XRAY"));
        }
        catch(RuntimeException e){
            return (List<MedicalReports>) ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/labReport/patient/{patientId}")
    public List<MedicalReports> getLabReportsByPatientId(@RequestHeader(value = "Authorization", required = false) String token,@PathVariable Long patientId) {
        ResponseEntity<String> validateToken = validateToken(token);
        if(!validateToken.getStatusCode().is2xxSuccessful()){
            return (List<MedicalReports>) validateToken;
        }try{
            return medicalReportService.getReportsByPatientAndType(patientId, ReportType.valueOf("LAB"));
        }
        catch(RuntimeException e){
            return (List<MedicalReports>) ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/surgeryReport/patient/{patientId}")
    public List<MedicalReports> getSurgeryReportsByPatientId(@RequestHeader(value = "Authorization", required = false) String token,@PathVariable Long patientId) {
        ResponseEntity<String> validateToken = validateToken(token);
        if(!validateToken.getStatusCode().is2xxSuccessful()){
            return (List<MedicalReports>) validateToken;
        }try{
            return medicalReportService.getReportsByPatientAndType(patientId, ReportType.valueOf("SURGERY"));
        }
        catch(RuntimeException e){
            return (List<MedicalReports>) ResponseEntity.badRequest().body(e.getMessage());
        }
    }




}

