package com.sdgp.MediPass.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name="BloodDonationRecords")
public class BloodDonationRecords {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long donationId;

    private String donationNumber;
    private LocalDate date;
    private String place;

    @ManyToOne      //since many blood donation records can be associated with one patient
//    @JoinColumn(name = "mediIdBD", nullable = false)     //FK referring to the MediId(PK) in Patient table
    @JoinColumn(name = "mediId", referencedColumnName = "mediId", nullable = false)
    private Patient patient;

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
}
