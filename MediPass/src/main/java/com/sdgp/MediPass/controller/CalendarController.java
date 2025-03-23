package com.sdgp.MediPass.controller;

import com.sdgp.MediPass.model.CalendarReminder;
import com.sdgp.MediPass.service.CalendarService;
import com.sdgp.MediPass.util.JwtUtil;
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

    @Autowired
    private JwtUtil jwtUtil;

    private String validateToken(String token) throws Exception {
        if(token == null || !token.startsWith("Bearer ")){
            throw new Exception("Missing or invalid Token");
        }
        String actualToken = token.substring(7);
        String mediId = jwtUtil.extractMediId(actualToken);
        if(mediId == null){
            throw new Exception("Invalid or expired token");
        }
        return mediId;
    }

    @ApiOperation(value = "Add reminders to the calendar")
    @PostMapping("/add")
    public ResponseEntity<CalendarReminder> addReminder(@RequestHeader("Authorization") String token, @RequestParam String description, @RequestParam LocalDateTime scheduledTime, @RequestParam Long mediId){
        try{
            String extractedMediId = validateToken(token);
            if (!extractedMediId.equals(String.valueOf(mediId))) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }
            CalendarReminder calendarReminder = calendarService.addReminder(description, scheduledTime, mediId);
            return ResponseEntity.status(HttpStatus.CREATED).body(calendarReminder);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @ApiOperation(value = "Delete reminders in the clendar")
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteReminder(@RequestHeader("Authorization") String token, @RequestParam Long reminderId, @RequestParam Long mediId) {
        try{
            String extractedMediId = validateToken(token);
            if(!extractedMediId.equals(String.valueOf(mediId))){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
            }

            calendarService.deleteReminder(reminderId);
            return ResponseEntity.ok("Reminder deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        }
    }

    @ApiOperation(value = "Get all reminders for a specific MediID")
    @GetMapping("/get")
    public ResponseEntity<List<CalendarReminder>> getReminders(@RequestHeader("Authorization") String token, @RequestParam Long mediId) {
        try{
            String extractedMediId = validateToken(token);
            if(!extractedMediId.equals(String.valueOf(mediId))){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }

            List<CalendarReminder> reminders = calendarService.getReminders(mediId);
            return ResponseEntity.ok(reminders);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }
}
