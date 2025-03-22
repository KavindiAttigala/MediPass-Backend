package com.sdgp.MediPass.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class CalendarReminder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reminderId;

    private String description;
    @Column(name = "scheulded_time")
    private LocalDateTime scheduledTime;

    private String email;

    private long mediID;
    @ManyToOne
    @JoinColumn (name="mediIDcalendar", nullable = false)
    private Patient patient;

    @Column(name = "reminder_sent")
    private boolean reminderSent;   // To avoid sending multiple reminders

    public Long getReminderId() {
        return reminderId;
    }

    public void setReminderId(Long reminderId) {
        this.reminderId = reminderId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(LocalDateTime scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public Patient getPatient() {
        return patient;
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
        return reminderSent;
    }

    public long getMediID() {
        return mediID;
    }

    public void setMediID(long mediID) {
        this.mediID = mediID;
    }
}
