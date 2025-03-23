package com.sdgp.MediPass.service;

import com.sdgp.MediPass.model.CalendarReminder;
import com.sdgp.MediPass.model.Patient;
import com.sdgp.MediPass.repository.CalendarRepository;
import com.sdgp.MediPass.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CalendarService {

    @Autowired
    private CalendarRepository calendarRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PatientRepository patientRepository;

    //schedule events and save it in the DB
    public CalendarReminder addReminder(String description, LocalDateTime scheduledTime, long mediId){
        // Fetch Patient using mediID
        Optional<Patient> patientOptional = patientRepository.findByMediId(mediId);
        if(patientOptional.isEmpty()){
            throw new IllegalArgumentException("Patient with mediId: "+ mediId+" not found.");
        }

        Patient patient = patientOptional.get();

        CalendarReminder calendarReminder = new CalendarReminder();

        calendarReminder.setDescription(description);
        calendarReminder.setScheduledTime(scheduledTime);
        calendarReminder.setEmail(patient.getEmail());
        calendarReminder.setReminderSent(false);
        calendarReminder.setPatient(patient);

        return calendarRepository.save(calendarReminder);
    }

    //scheduled task to send reminders(runs every hour)
    @Scheduled(fixedRate = 3600000) // Runs every hour
    public void sendReminders(long reminderId){
        LocalDateTime now = LocalDateTime.now();
        List<CalendarReminder> reminder = calendarRepository.findAll();     //Retrieve all scheduled reminders from the database

        if (reminder.isEmpty()) {
            throw new IllegalArgumentException("Reminder with ID " + reminderId + " not found.");
        }

        //foreach loop to iterate through the list of reminders ('reminder')
        for(CalendarReminder calendarReminder: reminder){
            boolean updated = false;

            //check for 1 day reminder
            if(!calendarReminder.isReminderSent() && calendarReminder.getScheduledTime().minusDays(1).isBefore(now)){
                emailService.sendEmail(calendarReminder.getEmail(), "Reminder "," Your scheduled event is in 1 day");
                updated = true;
            }

            //check for 6 hour reminder
            if(!calendarReminder.isReminderSent() && calendarReminder.getScheduledTime().minusHours(6).isBefore(now)){
                emailService.sendEmail(calendarReminder.getEmail(), "Reminder", "Your scheduled event is in 6 hours");
                updated = true;
            }

            // Save changes only if an email was sent
            if (updated) {
                calendarReminder.setReminderSent(true);
                calendarRepository.save(calendarReminder);
            }
        }
    }

    public void deleteReminder(long reminderId){
        Optional<CalendarReminder> reminderOptional = calendarRepository.findById(reminderId);
        if(reminderOptional.isEmpty()){
            throw new IllegalArgumentException("Reminder with mediId "+ reminderId+" not found.");
        }

        calendarRepository.delete(reminderOptional.get());
    }

    public List<CalendarReminder> getReminders(Long mediID) {
        return calendarRepository.findByMediID(mediID);
    }
}
