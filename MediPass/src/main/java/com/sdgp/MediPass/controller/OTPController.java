package com.sdgp.MediPass.controller;

import com.sdgp.MediPass.service.OTPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/forgotpassword/otp")
public class OTPController {

    @Autowired
    private OTPService otpService;

    @PostMapping("/send")
    public ResponseEntity<String> sendOTP(@RequestParam String nic, @RequestParam String mediId){
        try{
            return ResponseEntity.ok(otpService.sendOTP(nic,mediId));
        }catch(RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyOTP(@RequestParam String email, @RequestParam String otp) {
        return otpService.verifyOTP(email, otp)
                ? ResponseEntity.ok("OTP Verified")
                : ResponseEntity.badRequest().body("Invalid OTP");
    }
}
