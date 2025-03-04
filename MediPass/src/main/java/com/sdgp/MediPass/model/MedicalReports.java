package com.sdgp.MediPass.model;

import com.sdgp.MediPass.enums.ReportType;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class MedicalReports {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="patient_id", nullable = false)
    private Patient patient;

    @Enumerated(EnumType.STRING)
    private ReportType reportType;


    private String docName;
    private String specialization;
    private String slmc;
    private String nic;

    private String title;
    private String description;
    private String fileName;
    private String fileType;
    private String filePath;

    public MedicalReports(String title, String description, String fileName, String fileType, String filePath, ReportType reportType, Patient patient) {
        this.title = title;
        this.description = description;
        this.fileName = fileName;
        this.fileType = fileType;
        this.filePath = filePath;
        this.reportType = reportType;
        this.patient = patient;
    }

    public MedicalReports(Long medicalReportsId, Patient patient, String docName, String specialization, String slmc, String nic, String title, String description, String fileName, String fileType, String filePath) {
        this.id = medicalReportsId;
        this.patient = patient;
        this.docName = docName;
        this.specialization = specialization;
        this.slmc = slmc;
        this.nic = nic;
        this.title = title;
        this.description = description;
        this.fileName = fileName;
        this.fileType = fileType;
        this.filePath = filePath;
    }

    public MedicalReports() {

    }

    public MedicalReports(String title, String description, String originalFilename, String contentType, String filePath, Patient patient) {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getSlmc() {
        return slmc;
    }

    public void setSlmc(String slmc) {
        this.slmc = slmc;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public ReportType getReportType() {
        return reportType;
    }

    public void setReportType(ReportType reportType) {
        this.reportType = reportType;
    }
}
