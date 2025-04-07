package com.sdgp.MediPass.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table (name ="MedicalNotes")
public class MedicalNotes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn (name="patientId", nullable = false)      // Foreign key for patient
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "guestDoctorId", referencedColumnName = "docId", nullable = false)      // Foreign key for guestDoctor
    private GuestDoctor guestDoctor;

    private LocalDate date;
    private String textContent;

    public GuestDoctor getGuestDoctor() {
        return guestDoctor;
    }

    public void setGuestDoctor(GuestDoctor guestDoctor) {
        this.guestDoctor = guestDoctor;
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

}
