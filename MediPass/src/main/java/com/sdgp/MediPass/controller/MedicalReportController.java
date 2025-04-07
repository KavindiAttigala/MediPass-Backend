package com.sdgp.MediPass.controller;

import com.sdgp.MediPass.enums.ReportType;
import com.sdgp.MediPass.model.MedicalReports;
import com.sdgp.MediPass.model.Patient;
import com.sdgp.MediPass.service.MedicalReportService;
import com.sdgp.MediPass.util.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static com.sdgp.MediPass.controller.OTPController.extractMediId;

//@RestController
//@RequestMapping("/medipass/api/reports")
//@Api(value = "Medical Reports", description = "Managing medical reports of the patient")
//public class MedicalReportController {
//
//    @Autowired
//    private MedicalReportService medicalReportService;
//
//    @Autowired
//    private JwtUtil jwtUtil;
//
//
//    //validate the extracted token
//    private long extractMediId(String token) {
//        if (token == null || !token.startsWith("Bearer ")) {
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing or invalid token");
//        }
//        String actualToken = token.substring(7);
//        String mediId = jwtUtil.extractMediId(actualToken);
//        if (mediId == null) {
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid or expired token");
//        }
//        return Long.parseLong(mediId);
//    }
//
////    private ResponseEntity<String> validateToken(String token) {
////        return extractMediId(token, jwtUtil);
////    }
//
//
//    @ApiOperation(value = "Retrieving all medical reports")
//    @GetMapping
//    public ResponseEntity<?> getAllReports(@RequestHeader(value = "Authorization", required = false) String token) {
////        ResponseEntity<String> validateToken = validateToken(token);
//        long mediId = extractMediId(token);
//        try{
//            List<MedicalReports> reports = medicalReportService.getAllReports();
////            return (ResponseEntity<String>) medicalReportService.getAllReports();
//            return ResponseEntity.ok(reports);
//            }
//        catch(RuntimeException e){
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//
//    }
//
//    @ApiOperation(value = "Retrieving medical reports by mediId")
//    @GetMapping("/id")
//    public ResponseEntity<?> getReportById(@RequestHeader(value = "Authorization", required = false) String token) {
//        long mediId = extractMediId(token);
//        try {
//            Optional<MedicalReports> report = medicalReportService.getReportById(mediId);
//            return ResponseEntity.ok(report);
//        } catch (RuntimeException e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }
//
//    @ApiOperation(value = "Storing medical reports")
//    @PostMapping("/upload")
//    public ResponseEntity<?> uploadReport(
//            @RequestHeader(value = "Authorization", required = false) String token,
//            @RequestParam("file") MultipartFile file,
//            @RequestParam("title") String title,
//            @RequestParam("description") String description,
//            @RequestParam("patientId") Long patientId,
//            @RequestParam("reportType") ReportType reportType) {
//
//        long mediId = extractMediId(token);
//        try {
//            MedicalReports savedReport = medicalReportService.saveReportWithFile(file, title, description, reportType, patientId);
//            return ResponseEntity.ok(savedReport);
//        } catch (RuntimeException e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }
//
//    @GetMapping("/patient")
//    public List<MedicalReports> getReportsByPatient(@RequestHeader(value = "Authorization", required = false) String token,@RequestParam Patient patient) {
//        ResponseEntity<String> validateToken = validateToken(token);
//        if(!validateToken.getStatusCode().is2xxSuccessful()){
//            return (List<MedicalReports>) validateToken;
//        }try{
//            return medicalReportService.getReportsByPatientId(patient.getMediId());}
//
//        catch(RuntimeException e){
//            return (List<MedicalReports>) ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }
//
//    @GetMapping("/type/{reportType}")
//    public List<MedicalReports> getReportsByType(@RequestHeader(value = "Authorization", required = false) String token,@PathVariable ReportType reportType) {
//        ResponseEntity<String> validateToken = validateToken(token);
//        if(!validateToken.getStatusCode().is2xxSuccessful()){
//            return (List<MedicalReports>) validateToken;
//        }try{
//            return medicalReportService.getReportsByType(reportType);
//
//        }
//        catch(RuntimeException e){
//            return (List<MedicalReports>) ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }
//
//    @GetMapping("/patient/{patientId}/type/{reportType}")
//    public List<MedicalReports> getReportsByPatientAndType(@RequestHeader(value = "Authorization", required = false) String token,@PathVariable Long patientId, @PathVariable ReportType reportType) {
//        ResponseEntity<String> validateToken = validateToken(token);
//        if(!validateToken.getStatusCode().is2xxSuccessful()){
//            return (List<MedicalReports>) validateToken;
//        }try{
//            return medicalReportService.getReportsByPatientAndType(patientId, reportType);
//        }
//        catch(RuntimeException e){
//            return (List<MedicalReports>) ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }
//
//    @GetMapping("/xRay/patient/{patientId}")
//    public List<MedicalReports> getXRayReportsByPatientId(@RequestHeader(value = "Authorization", required = false) String token,@PathVariable Long patientId) {
//        ResponseEntity<String> validateToken = validateToken(token);
//        if(!validateToken.getStatusCode().is2xxSuccessful()){
//            return (List<MedicalReports>) validateToken;
//        }try{
//            return medicalReportService.getReportsByPatientAndType(patientId, ReportType.valueOf("XRAY"));
//        }
//        catch(RuntimeException e){
//            return (List<MedicalReports>) ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }
//    @GetMapping("/labReport/patient/{patientId}")
//    public List<MedicalReports> getLabReportsByPatientId(@RequestHeader(value = "Authorization", required = false) String token,@PathVariable Long patientId) {
//        ResponseEntity<String> validateToken = validateToken(token);
//        if(!validateToken.getStatusCode().is2xxSuccessful()){
//            return (List<MedicalReports>) validateToken;
//        }try{
//            return medicalReportService.getReportsByPatientAndType(patientId, ReportType.valueOf("LAB"));
//        }
//        catch(RuntimeException e){
//            return (List<MedicalReports>) ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }
//    @GetMapping("/surgeryReport/patient/{patientId}")
//    public List<MedicalReports> getSurgeryReportsByPatientId(@RequestHeader(value = "Authorization", required = false) String token,@PathVariable Long patientId) {
//        ResponseEntity<String> validateToken = validateToken(token);
//        if(!validateToken.getStatusCode().is2xxSuccessful()){
//            return (List<MedicalReports>) validateToken;
//        }try{
//            return medicalReportService.getReportsByPatientAndType(patientId, ReportType.valueOf("SURGERY"));
//        }
//        catch(RuntimeException e){
//            return (List<MedicalReports>) ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }
//
//
//
//
//}
@RestController
@RequestMapping("/medipass/api/reports")
@Api(value = "Medical Reports Controller", description = "Operations pertaining to medical reports in MediPass")
public class MedicalReportController {

    private final MedicalReportService medicalReportService;
    private final JwtUtil jwtUtil;

    public MedicalReportController(MedicalReportService medicalReportService, JwtUtil jwtUtil) {
        this.medicalReportService = medicalReportService;
        this.jwtUtil = jwtUtil;
    }

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

    @ApiOperation(value = "Upload a medical report")
    @PostMapping("/upload")
    public ResponseEntity<?> uploadReport(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
//            @RequestParam("patientId") Long patientId,
            @RequestParam("reportType") ReportType reportType) {

        long mediId = extractMediId(token);
        try {
            MedicalReports savedReport = medicalReportService.saveReport(file, title, description, reportType, mediId);
            return ResponseEntity.ok(savedReport);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/upload/XRAY")
    public ResponseEntity<?> uploadXRAYReport(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("description") String description) {

        long mediId = extractMediId(token);
        try {
            ReportType reportType = ReportType.XRAY;
            MedicalReports savedReport = medicalReportService.saveReport(file, title, description, reportType, mediId);
            return ResponseEntity.ok(savedReport);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/upload/LAB")
    public ResponseEntity<?> uploadLabReport(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("description") String description) {

        long mediId = extractMediId(token);
        try {
            ReportType reportType = ReportType.LAB;
            MedicalReports savedReport = medicalReportService.saveReport(file, title, description, reportType, mediId);
            return ResponseEntity.ok(savedReport);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/upload/SURGERY")
    public ResponseEntity<?> uploadSurgeryReport(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("description") String description) {

        long mediId = extractMediId(token);
        try {
            ReportType reportType = ReportType.SURGERY;
            MedicalReports savedReport = medicalReportService.saveReport(file, title, description, reportType, mediId);
            return ResponseEntity.ok(savedReport);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/view")
    public ResponseEntity<?> viewReport(@RequestHeader(value = "Authorization", required = false) String token) {
        long mediId = extractMediId(token);
        try {
            byte[] data = medicalReportService.downloadReport(mediId);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF); // assuming PDF
            headers.setContentDisposition(ContentDisposition.inline().filename("report_" + mediId + ".pdf").build());
            return new ResponseEntity<>(data, headers, HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @ApiOperation(value = "Retrieving all medical reports")
    @GetMapping
    public ResponseEntity<?> getAllReports(@RequestHeader(value = "Authorization", required = false) String token) {
        long mediId = extractMediId(token);
        try {
            List<MedicalReports> reports = medicalReportService.getAllReports();
            return ResponseEntity.ok(reports);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @ApiOperation(value = "Retrieve a medical report by ID")
    @GetMapping("/id")
    public ResponseEntity<?> getReportById(@RequestHeader(value = "Authorization", required = false) String token) {
        long mediId = extractMediId(token);
        try {
            MedicalReports report = medicalReportService.getReportById(mediId);
            return ResponseEntity.ok(report);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @ApiOperation(value = "Download a medical report file")
    @GetMapping("/download/id")
    public ResponseEntity<?> downloadReport(@RequestHeader(value = "Authorization", required = false) String token) {
        long mediId = extractMediId(token);
        try {
            byte[] data = medicalReportService.downloadReport(mediId);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDisposition(ContentDisposition.attachment().filename("report_" + mediId + ".pdf").build());
            return new ResponseEntity<>(data, headers, HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @ApiOperation(value = "Retrieve reports by type")
    @GetMapping("/type/{reportType}")
    public ResponseEntity<?> getReportsByType(@RequestHeader(value = "Authorization", required = false) String token,
                                              @PathVariable ReportType reportType) {
        long mediId = extractMediId(token);
        try {
            List<MedicalReports> reports = medicalReportService.getReportsByType(reportType);
            return ResponseEntity.ok(reports);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/xRay/patient")
    public ResponseEntity<?> getXRayReportsByPatientId(@RequestHeader(value = "Authorization", required = false) String token) {
        long mediId = extractMediId(token);
        try {
            return ResponseEntity.ok(medicalReportService.getReportsByPatientAndType(mediId, ReportType.XRAY));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/labReport/patient")
    public ResponseEntity<?> getLabReportsByPatientId(@RequestHeader(value = "Authorization", required = false) String token) {
        long mediId = extractMediId(token);
        try {
            return ResponseEntity.ok(medicalReportService.getReportsByPatientAndType(mediId, ReportType.LAB));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/surgeryReport/patient")
    public ResponseEntity<?> getSurgeryReportsByPatientId(@RequestHeader(value = "Authorization", required = false) String token) {
        long mediId = extractMediId(token);
        try {
            return ResponseEntity.ok(medicalReportService.getReportsByPatientAndType(mediId, ReportType.SURGERY));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

