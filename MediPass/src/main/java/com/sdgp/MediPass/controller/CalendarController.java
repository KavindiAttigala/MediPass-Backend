package com.sdgp.MediPass.controller;

import com.sdgp.MediPass.model.CalendarReminder;
import com.sdgp.MediPass.service.CalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/calendar-reminders")
public class CalendarController {

    @Autowired
    private CalendarService calendarService;

    public ResponseEntity<CalendarReminder> addReminder(@RequestParam String description, @RequestParam String start, @RequestParam String end, @RequestParam String email){
        CalendarReminder calendarReminder = calendarService.addReminder(description, start,end,email);
        return ResponseEntity.status(HttpStatus.CREATED).body(calendarReminder);
    }

}
