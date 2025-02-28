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
@Table (name ="MedicalNotes")
public class MedicalNotes {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long medicalNotes;

    @ManyToOne
    @JoinColumn (name="mediIDnotes", nullable = false)
    private Patient patient;

    private LocalDate date;

    private String textContent;
    private String fileName;
    private String fileType;
    private String filePath;

}
