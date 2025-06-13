package com.sdgp.MediPass.model;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    private String birthday;
    private String address;
    private String bloodGroup;
    private String gender;

    @Column(columnDefinition = "double precision")
    private double height;

    @Column(columnDefinition = "double precision")
    private double weight;

    private String allergy;

    @Column(columnDefinition = "TEXT")
    @Lob
    private String profilePicture;

    // One patient can have many medical notes.
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<MedicalNotes> medicalNotes;

    // Constructors
    public Patient() { }

    public Patient(String firstName, String lastName, String email, String nic, String contactNumber, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.nic = nic;
        this.contactNumber = contactNumber;
        this.password = password;
    }

    public Patient(long mediId, String firstName, String lastName, String email, String nic, String contactNumber, String password, String role, String birthday, String address, String bloodGroup, String gender, double height, double weight, String allergy, String profilePicture, List<MedicalNotes> medicalNotes) {
        this.mediId = mediId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.nic = nic;
        this.contactNumber = contactNumber;
        this.password = password;
        this.role = role;
        this.birthday = birthday;
        this.address = address;
        this.bloodGroup = bloodGroup;
        this.gender = gender;
        this.height = height;
        this.weight = weight;
        this.allergy = allergy;
        this.profilePicture = profilePicture;
        this.medicalNotes = medicalNotes;
    }

    // Getters and Setters

    public long getMediId() {
        return mediId;
    }

    public void setMediId(long mediId) {
        this.mediId = mediId;
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

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getAllergy() {
        return allergy;
    }

    public void setAllergy(String allergy) {
        this.allergy = allergy;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public List<MedicalNotes> getMedicalNotes() {
        return medicalNotes;
    }

    public void setMedicalNotes(List<MedicalNotes> medicalNotes) {
        this.medicalNotes = medicalNotes;
    }

    // Expose a computed "fullName" in the JSON response.
    @JsonProperty("fullName")
    @Transient
    public String getFullName() {
        // Safely concatenate firstName and lastName
        String fName = (firstName != null) ? firstName : "";
        String lName = (lastName != null) ? lastName : "";
        return (fName + " " + lName).trim();
    }
}
