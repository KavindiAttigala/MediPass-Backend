package com.sdgp.MediPass.controller;

import com.sdgp.MediPass.model.Patient;
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
    @PostMapping("/sendOTP")
    public ResponseEntity<String> sendOTP(@RequestParam String nic, @RequestParam long mediId){
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
    @PostMapping("/verifyOTP")
    public ResponseEntity<String> verifyOTP(@RequestParam String email, @RequestParam String otp) {
        return otpService.verifyOTP(email, otp)
                ? ResponseEntity.ok("OTP Verified")
                : ResponseEntity.badRequest().body("Invalid OTP");
    }

    @ApiOperation(value = "Reset password after OTP verification")
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam Long mediId, @RequestParam String email, @RequestParam String otp, @RequestParam String newPassword) {
        if (!otpService.verifyOTP(email, otp)) {
            return ResponseEntity.badRequest().body("Invalid OTP");
        }
        boolean passwordUpdated = patientService.updatePassword(mediId, newPassword);
        return passwordUpdated
                ? ResponseEntity.ok("Password reset successfully")
                : ResponseEntity.badRequest().body("Failed to reset password");
    }
    
}
