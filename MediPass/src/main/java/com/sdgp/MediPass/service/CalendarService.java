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

//    @Autowired
//    private CalendarRepository calendarRepo;
//
//    @Autowired
//    private PatientRepository patientRepo;
//
//    public String createReminder(Long mediId,String description, LocalDate date) throws IOException, GeneralSecurityException{
//        Optional<Patient> patientOptional = patientRepo.findByMediId(mediId);
//
//        if (!patientOptional.isPresent()) {
//            return "Patient not found.";
//        }
//
//        Patient patient = patientOptional.get();
//        String patientEmail = patient.getEmail();
//
//
//    }

    @Autowired
    private CalendarRepository calendarRepository;

    @Autowired
    private EmailReminderService emailReminderService;

    //schedule events and save it in the DB
    public CalendarReminder addReminder(String description, String start, String end, String email){
        CalendarReminder calendarReminder = new CalendarReminder();
        calendarReminder.setDescription(description);
        calendarReminder.setStart(LocalDateTime.parse(start));
        calendarReminder.setEnd(LocalDateTime.parse(end));
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
            if(!calendarReminder.isReminderSent() && calendarReminder.getStart().minusDays(1).isBefore(now)){
                emailReminderService.sendEmail(calendarReminder.getEmail(), "Reminder "," Your scheduled event is in 1 day");
                calendarReminder.setReminderSent(true);
                calendarRepository.save(calendarReminder);
            }

            //check for 6 hour reminder
            if(!calendarReminder.isReminderSent() && calendarReminder.getStart().minusHours(6).isBefore(now)){
                emailReminderService.sendEmail(calendarReminder.getEmail(), "Reminder", "Your scheduled event is in 6 hours");
                calendarReminder.setReminderSent(true);
                calendarRepository.save(calendarReminder);
            }
        }
    }

}
