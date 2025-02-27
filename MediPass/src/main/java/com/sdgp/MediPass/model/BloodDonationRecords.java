package com.sdgp.MediPass.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name="BloodDonationRecords")
public class BloodDonationRecords {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long donationId;

    private String donationNumber;
    private LocalDate date;
    private String place;

    @ManyToOne      //since many blood donation records can be associated with one patient
    @JoinColumn(name = "mediIdBD", nullable = false)     //FK referring to the MediId(PK) in Patient table
    private Patient patient;

}
