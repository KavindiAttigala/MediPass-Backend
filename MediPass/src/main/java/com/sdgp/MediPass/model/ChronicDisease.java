package com.sdgp.MediPass.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

}
