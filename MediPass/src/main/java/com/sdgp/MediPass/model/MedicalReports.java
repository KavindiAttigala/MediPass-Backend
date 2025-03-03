package com.sdgp.MediPass.model;

import jakarta.persistence.*;

import java.time.LocalDate;

public class MedicalReports {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long medicalNotes;

    @ManyToOne
    @JoinColumn(name="mediIDnotes", nullable = false)
    private Patient patient;

    private LocalDate date;
    private String docName;
    private String specialization;
    private String slmc;
    private String nic;
    private String textContent;
    private String fileName;
    private String fileType;
    private String filePath;

    public Long getMedicalNotes() {
        return medicalNotes;
    }

    public void setMedicalNotes(Long medicalNotes) {
        this.medicalNotes = medicalNotes;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
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

    public String getTextContent() {
        return textContent;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
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
}
