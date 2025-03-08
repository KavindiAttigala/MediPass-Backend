package com.sdgp.MediPass.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class VaccinationRecords {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long vaccinationId;
    private String vaccinationType;
    private LocalDate date;
    private String age;
    private String batchNo;
    private String description;
    private String specialNotes;

    @ManyToOne
    @JoinColumn(name = "mediIdVaccination" , nullable = false)
    private Patient patient;

    public VaccinationRecords(String vaccinationType, LocalDate date, String age, String batchNo, String description, String specialNotes, Patient patient) {
        this.vaccinationType = vaccinationType;
        this.date = date;
        this.age = age;
        this.batchNo = batchNo;
        this.description = description;
        this.specialNotes = specialNotes;
        this.patient = patient;
    }

    public VaccinationRecords() {

    }

    public long getVaccinationId() {
        return vaccinationId;
    }

    public void setVaccinationId(long vaccinationId) {
        this.vaccinationId = vaccinationId;
    }

    public String getVaccinationType() {
        return vaccinationType;
    }

    public void setVaccinationType(String vaccinationType) {
        this.vaccinationType = vaccinationType;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSpecialNotes() {
        return specialNotes;
    }

    public void setSpecialNotes(String specialNotes) {
        this.specialNotes = specialNotes;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }
}
