package com.sdgp.MediPass.controller;

import com.sdgp.MediPass.model.CalendarReminder;
import com.sdgp.MediPass.service.CalendarService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/medipass/calendar-reminders")
@Api(value="Medical calendar", description = "Scheduling events on the medical calendar to get reminders")
public class CalendarController {

    @Autowired
    private CalendarService calendarService;

    @ApiOperation(value = "Add reminders to the calendar")
    @PostMapping("/add")
    public ResponseEntity<CalendarReminder> addReminder(@RequestParam String description, @RequestParam LocalDateTime scheduledTime, @RequestParam Long mediID){
        CalendarReminder calendarReminder = calendarService.addReminder(description, scheduledTime, mediID);
        return ResponseEntity.status(HttpStatus.CREATED).body(calendarReminder);
    }

    @ApiOperation(value = "Delete reminders in the clendar")
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteReminder(@RequestParam Long reminderId) {
        calendarService.deleteReminder(reminderId);
        return ResponseEntity.ok("Reminder deleted successfully");
    }

    @ApiOperation(value = "Get all reminders for a specific MediID")
    @GetMapping("/get")
    public ResponseEntity<List<CalendarReminder>> getReminders(@RequestParam Long mediID) {
        List<CalendarReminder> reminders = calendarService.getReminders(mediID);
        return ResponseEntity.ok(reminders);
    }
}
