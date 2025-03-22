package com.sdgp.MediPass.controller;

import com.sdgp.MediPass.model.GuestDoctor;
import com.sdgp.MediPass.model.Patient;
import com.sdgp.MediPass.service.GuestDoctorService;
import com.sdgp.MediPass.service.OTPService;
import com.sdgp.MediPass.service.PatientService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/guest-logging")
@Api(value="Guest Doctor Login", description = "Storing guest login information")
public class GuestDoctorController {
    @Autowired
    private GuestDoctorService guestDoctorService;
    @Autowired
    private PatientService patientService;
    @Autowired
    private OTPService otpService;

    @ApiOperation(value = "Storing guest doctor login info in the DB")
    @PostMapping("/access")
    public ResponseEntity<String> doctorAccess(@RequestParam long mediId, @RequestParam String otp, @RequestBody GuestDoctor guestDoctor){
        Optional<Patient> patientOptional =patientService.getUserByMediId(mediId);
        if(patientOptional.isEmpty()){
            return ResponseEntity.badRequest().body("Invalid MediID");
        }

        //verify OTP access
        if(!otpService.verifyOTP(mediId,otp)){
            return ResponseEntity.badRequest().body("Invalid OTP");
        }

        //each guest doctor session is stored before adding medical notes
        GuestDoctor savedDoctor = guestDoctorService.saveDoctor(guestDoctor);
        return ResponseEntity.ok(String.valueOf(savedDoctor));
    }
}
