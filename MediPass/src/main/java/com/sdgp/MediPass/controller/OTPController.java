package com.sdgp.MediPass.controller;

import com.sdgp.MediPass.service.OTPService;
import com.sdgp.MediPass.service.PatientService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/forgotpassword/otp")
@Api(value = "OTP for forgot password", description = "Sending OTP for user emails")
public class OTPController {

    @Autowired
    private OTPService otpService;

    @Autowired
    private PatientService patientService;

    @ApiOperation(value = "Uploading nic and mediId to get verify the user")
    @PostMapping("/send")
    public ResponseEntity<String> sendOTP(@RequestParam String nic, @RequestParam long mediId){
//        try{
//            return ResponseEntity.ok(otpService.sendOTP(nic,mediId));
//        }catch(RuntimeException e){
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }

        try {
            String email = patientService.verifyUserAndGetEmail(nic, mediId);
            if (email == null) {
                return ResponseEntity.badRequest().body("User not found or invalid NIC/MediID.");
            }
            otpService.sendOTP(nic, mediId);
            return ResponseEntity.ok("OTP sent to " + email);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @ApiOperation(value = "Verify OTP sent to user's email")
    @PostMapping("/verify")
    public ResponseEntity<String> verifyOTP(@RequestParam String email, @RequestParam String otp) {
        return otpService.verifyOTP(email, otp)
                ? ResponseEntity.ok("OTP Verified")
                : ResponseEntity.badRequest().body("Invalid OTP");
    }
}
