package com.sdgp.MediPass.service;

import com.sdgp.MediPass.enums.ReportType;
import com.sdgp.MediPass.model.MedicalReports;
import com.sdgp.MediPass.model.Patient;
import com.sdgp.MediPass.repository.MedicalReportRepository;
import com.sdgp.MediPass.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

//@Service
//public class MedicalReportService {
//    @Autowired
//    private MedicalReportRepository medicalReportRepository;
//
//    private final String UPLOAD_DIR = "uploads/";
//
//    public List<MedicalReports> getAllReports() {
//        return medicalReportRepository.findAll();
//    }
//
//    public Optional<MedicalReports> getReportById(Long id) {
//        return medicalReportRepository.findById(id);
//    }
//
//    public List<MedicalReports> getReportsByPatientId(Long patientId) {
//        return medicalReportRepository.findByPatientMediId(patientId);
//    }
//
//    public MedicalReports saveReport(MedicalReports report) {
//        return medicalReportRepository.save(report);
//    }
//
//    public void deleteReport(Long id) {
//        medicalReportRepository.deleteById(id);
//    }
//
//    // Method to handle file upload
//    public MedicalReports saveReportWithFile(MultipartFile file, String title, String description, Patient patient) throws IOException {
//        String filePath = UPLOAD_DIR + file.getOriginalFilename();
//        File uploadFile = new File(filePath);
//        uploadFile.getParentFile().mkdirs();
//        file.transferTo(uploadFile);
//
//        MedicalReports report = new MedicalReports(
//                title, description, file.getOriginalFilename(), file.getContentType(), filePath, patient
//        );
//
//        return medicalReportRepository.save(report);
//    }
//
//    // Method to retrieve a file
//    public Resource getFile(String fileName) throws MalformedURLException {
//        Path filePath = Paths.get(UPLOAD_DIR).resolve(fileName).normalize();
//        return new UrlResource(filePath.toUri());
//    }
//
//    public List<MedicalReports> getReportsByType(ReportType reportType) {
//        return medicalReportRepository.findByReportType(reportType);
//    }
//
//    public List<MedicalReports> getReportsByPatientAndType(Long patientId, ReportType reportType) {
//        return medicalReportRepository.findByPatientMediIdAndReportType(patientId, reportType);
//    }
//}
@Service
public class MedicalReportService {

    private final MedicalReportRepository medicalReportRepository;
    private final PatientRepository patientRepository;
    private final String uploadDir = "uploads/medical-reports/";

    public MedicalReportService(MedicalReportRepository medicalReportRepository, PatientRepository patientRepository) {
        this.medicalReportRepository = medicalReportRepository;
        this.patientRepository = patientRepository;
    }

    public MedicalReports saveReport(MultipartFile file, String title, String description, ReportType reportType, Long patientId) {
        try {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            File targetFile = new File(uploadDir + fileName);
            targetFile.getParentFile().mkdirs();
            try (FileOutputStream fos = new FileOutputStream(targetFile)) {
                fos.write(file.getBytes());
            }

            Patient patient = patientRepository.findById(patientId)
                    .orElseThrow(() -> new RuntimeException("Patient not found"));

            MedicalReports report = new MedicalReports(null, title, description, reportType, patient, targetFile.getPath());
            return medicalReportRepository.save(report);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save report", e);
        }
    }

    public List<MedicalReports> getAllReports() {
        return medicalReportRepository.findAll();
    }

    public MedicalReports getReportById(Long id) {
        return medicalReportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Report not found"));
    }

    public byte[] downloadReport(Long id) {
        try {
            MedicalReports report = getReportById(id);
            return java.nio.file.Files.readAllBytes(new File(report.getFileUrl()).toPath());
        } catch (IOException e) {
            throw new RuntimeException("Failed to read report file", e);
        }
    }

    public List<MedicalReports> getReportsByType(ReportType reportType) {
        return medicalReportRepository.findByReportType(reportType);
    }
    public List<MedicalReports> getReportsByPatientAndType(Long patientId, ReportType reportType) {
        return medicalReportRepository.findByPatientMediIdAndReportType(patientId, reportType);
    }
}
