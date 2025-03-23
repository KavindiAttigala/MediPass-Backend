package com.sdgp.MediPass.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Optional;

@Entity
@Table(name="BloodDonationRecords")
public class BloodDonationRecords {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long donationId;

    private String donationNumber;
    private LocalDate date;
    private String place;
    private long mediId;

    @ManyToOne      //since many blood donation records can be associated with one patient
//    @JoinColumn(name = "mediIdBD", nullable = false)     //FK referring to the MediId(PK) in Patient table
    @JoinColumn(name = "mediId", referencedColumnName = "mediId", nullable = false)
    private Patient patient;

    public BloodDonationRecords(Long donationId, String donationNumber, LocalDate date, String place, long mediId, Patient patient) {
        this.donationId = donationId;
        this.donationNumber = donationNumber;
        this.date = date;
        this.place = place;
        this.mediId = mediId;
        this.patient = patient;
    }

    public BloodDonationRecords() {

    }

    public BloodDonationRecords(Long donationId, String donationNumber, LocalDate date, String place, Patient patient) {
        this.donationId = donationId;
        this.donationNumber = donationNumber;
        this.date = date;
        this.place = place;
        this.patient=patient;
    }

    public Long getDonationId() {
        return donationId;
    }

    public void setDonationId(Long donationId) {
        this.donationId = donationId;
    }

    public String getDonationNumber() {
        return donationNumber;
    }

    public void setDonationNumber(String donationNumber) {
        this.donationNumber = donationNumber;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public long getMediId() {
        return mediId;
    }

    public void setMediId(long mediId) {
        this.mediId = mediId;
    }

    public void setPatient(Optional<Patient> patient) {
    }
}
