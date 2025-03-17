package com.sdgp.MediPass.model;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import javax.annotation.processing.Generated;
import java.util.List;

@Entity
@Table(name="Patient")
public class Patient {
    @Id
    @GeneratedValue(generator = "IdGenerator")
    @GenericGenerator(name = "IdGenerator", strategy = "com.sdgp.MediPass.util.IdGenerator")
    private long mediId;

    private String firstName;
    private String lastName;
    private String email;
    private String nic;
    private String contactNumber;
    private String password;
    private String role;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    private List<CalendarReminder> reminders;

    public Patient(String firstName, String lastName, String email, String nic, String contactNumber, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.nic = nic;
        this.contactNumber = contactNumber;
        this.password = password;
    }

    public Patient() {

    }

    //one patient can have many medical notes where medical notes is mapped by 'patient' field.
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    private List<MedicalNotes> medicalNotes;

    public List<MedicalNotes> getMedicalNotes() {
        return medicalNotes;
    }

    public void setMedicalNotes(List<MedicalNotes> medicalNotes) {
        this.medicalNotes = medicalNotes;
    }

    public long getMediId() {
        return mediId;
    }

    public void setMediId(long mediId) {
        mediId = mediId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
