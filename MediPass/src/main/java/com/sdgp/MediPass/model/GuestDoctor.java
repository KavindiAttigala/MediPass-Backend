package com.sdgp.MediPass.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class GuestDoctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long docId;

    private String docName;
    private String specialization;
    private String slmc;
    private String docNic;
    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "mediId", nullable = false)
    private Patient patient;  // Link GuestDoctor to the Patient

    //one-to-many relationship between guestDoctor and MedicalNotes where medical notes is mapped by 'DoctorGuestLog' field
    @OneToMany(mappedBy = "guestDoctor", cascade = CascadeType.ALL)
    private List<MedicalNotes> medicalNotes;

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public List<MedicalNotes> getMedicalNotes() {
        return medicalNotes;
    }

    public void setMedicalNotes(List<MedicalNotes> medicalNotes) {
        this.medicalNotes = medicalNotes;
    }

    public long getDocId() {
        return docId;
    }

    public void setDocId(long docId) {
        this.docId = docId;
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

    public String getDocNic() {
        return docNic;
    }

    public void setDocNic(String docNic) {
        this.docNic = docNic;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
