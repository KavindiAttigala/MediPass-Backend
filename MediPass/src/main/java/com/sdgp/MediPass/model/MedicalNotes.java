package com.sdgp.MediPass.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table (name ="MedicalNotes")
public class MedicalNotes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long medicalNotes;

    @ManyToOne
    @JoinColumn (name="mediIDnotes", nullable = false)
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "doctor_guest_log_id", referencedColumnName = "id", nullable = false)
    private GuestDoctor guestDoctor;

    private LocalDate date;
    private String textContent;
    private String fileName;
    private String fileType;
    private String filePath;

    public GuestDoctor getGuestDoctor() {
        return guestDoctor;
    }

    public void setGuestDoctor(GuestDoctor guestDoctor) {
        this.guestDoctor = guestDoctor;
    }

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
