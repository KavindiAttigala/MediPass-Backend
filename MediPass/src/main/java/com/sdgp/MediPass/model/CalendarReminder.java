package com.sdgp.MediPass.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class CalendarReminder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;
    private LocalDateTime start;
    private LocalDateTime end;
    private String email;

    @ManyToOne
    @JoinColumn (name="mediIDnotes", nullable = false)
    private Patient patient;

    @Column(name = "reminder_sent")
    private boolean reminderSent; // To avoid sending multiple reminders

    public void setId(Long id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public void setReminderSent(boolean reminderSent) {
        this.reminderSent = reminderSent;
    }

    public boolean isReminderSent() {
        return false;
    }
}
