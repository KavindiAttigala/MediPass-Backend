package com.sdgp.MediPass.controller;

import com.sdgp.MediPass.model.GuestDoctor;
import com.sdgp.MediPass.service.GuestDoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/guest-logging")
public class GuestDoctorController {
    @Autowired
    private GuestDoctorService guestDoctorService;

    public ResponseEntity<GuestDoctor> doctorAccess(@RequestBody GuestDoctor guestDoctor){
        return ResponseEntity.ok(guestDoctorService.saveDoctor(guestDoctor));
    }
}
