package com.sdgp.MediPass.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name="ChronicDisease")
public class ChronicDisease {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn (name="mediIDchronical", nullable = false)
    private Patient patient;

    private String diseaseName;
    private String medication;
    private int dosage;
    private char startDate;
    private char endDate;
    private LocalDate date;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public String getDiseaseName() {
        return diseaseName;
    }

    public void setDiseaseName(String diseaseName) {
        this.diseaseName = diseaseName;
    }

    public String getMedication() {
        return medication;
    }

    public void setMedication(String medication) {
        this.medication = medication;
    }

    public int getDosage() {
        return dosage;
    }

    public void setDosage(int dosage) {
        this.dosage = dosage;
    }

    public char getStartDate() {
        return startDate;
    }

    public void setStartDate(char startDate) {
        this.startDate = startDate;
    }

    public char getEndDate() {
        return endDate;
    }

    public void setEndDate(char endDate) {
        this.endDate = endDate;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
