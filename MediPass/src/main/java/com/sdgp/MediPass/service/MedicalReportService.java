package com.sdgp.MediPass.service;

import com.sdgp.MediPass.enums.ReportType;
import com.sdgp.MediPass.model.MedicalReports;
import com.sdgp.MediPass.model.Patient;
import com.sdgp.MediPass.repository.MedicalReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class MedicalReportService {
    @Autowired
    private MedicalReportRepository medicalReportRepository;

    private final String UPLOAD_DIR = "uploads/";

    public List<MedicalReports> getAllReports() {
        return medicalReportRepository.findAll();
    }

    public Optional<MedicalReports> getReportById(Long id) {
        return medicalReportRepository.findById(id);
    }

    public List<MedicalReports> getReportsByPatientId(Long patientId) {
        return medicalReportRepository.findByPatientMediId(patientId);
    }

    public MedicalReports saveReport(MedicalReports report) {
        return medicalReportRepository.save(report);
    }

    public void deleteReport(Long id) {
        medicalReportRepository.deleteById(id);
    }

    // Method to handle file upload
    public MedicalReports saveReportWithFile(MultipartFile file, String title, String description, Patient patient) throws IOException {
        String filePath = UPLOAD_DIR + file.getOriginalFilename();
        File uploadFile = new File(filePath);
        uploadFile.getParentFile().mkdirs();
        file.transferTo(uploadFile);

        MedicalReports report = new MedicalReports(
                title, description, file.getOriginalFilename(), file.getContentType(), filePath, patient
        );

        return medicalReportRepository.save(report);
    }

    // Method to retrieve a file
    public Resource getFile(String fileName) throws MalformedURLException {
        Path filePath = Paths.get(UPLOAD_DIR).resolve(fileName).normalize();
        return new UrlResource(filePath.toUri());
    }

    public List<MedicalReports> getReportsByType(ReportType reportType) {
        return medicalReportRepository.findByReportType(reportType);
    }

    public List<MedicalReports> getReportsByPatientAndType(Long patientId, ReportType reportType) {
        return medicalReportRepository.findByPatientMediIdAndReportType(patientId, reportType);
    }
}
