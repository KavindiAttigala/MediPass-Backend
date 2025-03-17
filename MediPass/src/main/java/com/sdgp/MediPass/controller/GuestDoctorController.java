package com.sdgp.MediPass.controller;

import com.sdgp.MediPass.model.GuestDoctor;
import com.sdgp.MediPass.service.GuestDoctorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/guest-logging")
@Api(value="Guest Doctor Login", description = "Storing guest login information")
public class GuestDoctorController {
    @Autowired
    private GuestDoctorService guestDoctorService;

    @ApiOperation(value = "Storing guest doctor login info in the DB")
    @PostMapping("/access")
    public ResponseEntity<GuestDoctor> doctorAccess(@RequestBody GuestDoctor guestDoctor){
        return ResponseEntity.ok(guestDoctorService.saveDoctor(guestDoctor));
    }
}
