package com.sdgp.MediPass.service;

import com.sdgp.MediPass.model.CalendarReminder;
import com.sdgp.MediPass.model.Patient;
import com.sdgp.MediPass.repository.CalendarRepository;
import com.sdgp.MediPass.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CalendarService {

    @Autowired
    private CalendarRepository calendarRepository;

    @Autowired
    private EmailReminderService emailReminderService;

    @Autowired
    private PatientRepository patientRepository;

    //schedule events and save it in the DB
    public CalendarReminder addReminder(String description, String start, String end, String email, long mediId){
        // Fetch Patient using mediID
        Optional<Patient> patientOptional = patientRepository.findByMediId(mediId);
        if(patientOptional.isEmpty()){
            throw new IllegalArgumentException("Patient with mediId "+ mediId+" not found.");
        }

        Patient patient = patientOptional.get();

        CalendarReminder calendarReminder = new CalendarReminder();
        calendarReminder.setDescription(description);
        calendarReminder.setStartTime(LocalDateTime.parse(start));
        calendarReminder.setEndTime(LocalDateTime.parse(end));
        calendarReminder.setEmail(email);
        calendarReminder.setReminderSent(false);

        return calendarRepository.save(calendarReminder);
    }

    //scheduled task to send reminders
    public void sendReminders(){
        LocalDateTime now = LocalDateTime.now();
        List<CalendarReminder> reminder = calendarRepository.findAll();

        for(CalendarReminder calendarReminder: reminder){
            //check for 1 day reminder
            if(!calendarReminder.isReminderSent() && calendarReminder.getStartTime().minusDays(1).isBefore(now)){
                emailReminderService.sendEmail(calendarReminder.getEmail(), "Reminder "," Your scheduled event is in 1 day");
                calendarReminder.setReminderSent(true);
                calendarRepository.save(calendarReminder);
            }

            //check for 6 hour reminder
            if(!calendarReminder.isReminderSent() && calendarReminder.getStartTime().minusHours(6).isBefore(now)){
                emailReminderService.sendEmail(calendarReminder.getEmail(), "Reminder", "Your scheduled event is in 6 hours");
                calendarReminder.setReminderSent(true);
                calendarRepository.save(calendarReminder);
            }
        }
    }

    public void deleteReminder(long id){
        Optional<CalendarReminder> reminderOptional = calendarRepository.findById(id);
        if(reminderOptional.isEmpty()){
            throw new IllegalArgumentException("Reminder with mediId "+ id+" not found.");
        }

        calendarRepository.delete(reminderOptional.get());
    }

}
