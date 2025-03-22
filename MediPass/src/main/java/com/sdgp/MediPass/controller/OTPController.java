package com.sdgp.MediPass.controller;

import com.sdgp.MediPass.model.Patient;
import com.sdgp.MediPass.service.OTPService;
import com.sdgp.MediPass.service.PatientService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
    //send OTP when forgot password case by verifying the user using the NIC and MediID
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
    //verify the OTP sent to the user
    public ResponseEntity<String> verifyOTP(@RequestParam String email, @RequestParam String otp) {
        return otpService.verifyOTP(email, otp)
                ? ResponseEntity.ok("OTP Verified")         //If verifyOTP() returns true
                : ResponseEntity.badRequest().body("Invalid OTP");          //If verifyOTP(email, otp) returns false
    }

    @ApiOperation(value = "Reset password after OTP verification")
    @PostMapping("/reset-password")
    //update the new password
    public ResponseEntity<String> resetPassword(@RequestParam long mediId, @RequestParam String email, @RequestParam String otp, @RequestParam String newPassword) {
        if (!otpService.verifyOTP(email, otp)) {
            return ResponseEntity.badRequest().body("Invalid OTP");
        }
        boolean passwordUpdated = patientService.updatePassword(mediId, newPassword);
        return passwordUpdated
                ? ResponseEntity.ok("Password reset successfully")          //If updatePassword() returns true
                : ResponseEntity.badRequest().body("Failed to reset password");
    }

    @ApiOperation(value = "Send OTP for doctor access request")
    @PostMapping("/sendDoctorAccessOTP")
    //send the doctor login otp
    public ResponseEntity<String> sendDoctorAccessOTP(@RequestParam long mediId){
        try{
            return ResponseEntity.ok(otpService.sendDoctorAccessOTP(mediId));
        }catch(RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @ApiOperation(value = "Verify the OTP for the doctor login")
    @PostMapping("/doctor-access")
    //verify the doctor login OTP
    public ResponseEntity<String> verfyDoctorAccessOTP(@RequestParam long mediId, @RequestParam String otp){
        List<Patient> patient = patientService.getPatientByMediId(mediId);
        if(patient.isEmpty()){
            return ResponseEntity.badRequest().body("Invalid mediId or OTP");
        }

        String email = patient.get(0).getEmail();
        return otpService.verifyOTP(email,otp)
                ? ResponseEntity.ok("OTP Verified")         //If verifyOTP() returns true
                : ResponseEntity.badRequest().body("Invalid OTP");

    }
    
}
