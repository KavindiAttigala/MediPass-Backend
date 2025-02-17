package com.sdgp.MediPass.model;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import javax.annotation.processing.Generated;

@Entity
@Table(name="Patient")
public class Patient {
    @Id
    @GeneratedValue(generator = "IdGenerator")
    @GenericGenerator(name = "IdGenerator", strategy = "com.sdgp.MediPass.util.IdGenerator")
    private long MediId;

    private String firstName;
    private String lastName;
    private String email;
    private String nic;
    private String contactNumber;
    private String password;

    public long getMediId() {
        return MediId;
    }

    public void setMediId(long mediId) {
        MediId = mediId;
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
}
