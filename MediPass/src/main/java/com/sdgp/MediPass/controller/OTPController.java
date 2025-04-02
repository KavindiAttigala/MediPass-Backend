package com.sdgp.MediPass.controller;

import com.sdgp.MediPass.model.Patient;
import com.sdgp.MediPass.service.OTPService;
import com.sdgp.MediPass.service.PatientService;
import com.sdgp.MediPass.util.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/medipass/otp")
@Api(value = "OTP for forgot password", description = "Sending OTP for user emails")
public class OTPController {

    @Autowired
    private OTPService otpService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private JwtUtil jwtUtil;

    //validate the extracted token
    private ResponseEntity<String> validateToken(String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid token");
        }
        String actualToken = token.substring(7);
        String mediId = jwtUtil.extractMediId(actualToken);
        if (mediId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        }
        return ResponseEntity.ok(mediId);  // Return extracted MediID if valid
    }


    @ApiOperation(value = "Uploading nic and mediId to get verify the user")
    @PostMapping("/sendOTP")
    //send OTP when forgot password case by verifying the user using the NIC and MediID
    public ResponseEntity<String> sendOTP(@RequestParam String nic, @RequestParam long mediId){
        try {
            return ResponseEntity.ok(otpService.sendOTP(nic, mediId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @ApiOperation(value = "Verify OTP sent to user's email")
    @PostMapping("/verifyOTP")
    //verify the OTP sent to the user
    public ResponseEntity<String> verifyOTP(@RequestParam long mediId, @RequestParam String otp) {
        return otpService.verifyOTP(mediId, otp)
                ? ResponseEntity.ok("OTP Verified")         //If verifyOTP() returns true
                : ResponseEntity.badRequest().body("Invalid OTP");          //If verifyOTP() returns false
    }

    @ApiOperation(value = "Reset password after OTP verification")
    @PostMapping("/reset-password")
    //update the new password (forgot-password)
    public ResponseEntity<String> resetPassword(@RequestParam long mediId, @RequestParam String newPassword) {
        boolean passwordUpdated = patientService.updatePassword(mediId, newPassword);
        return passwordUpdated
                ? ResponseEntity.ok("Password reset successfully")          //If updatePassword() returns true
                : ResponseEntity.badRequest().body("Failed to reset password");
    }

    @ApiOperation(value = "Send OTP for doctor access request")
    @PostMapping("/sendDoctorAccessOTP")
    //send the doctor login otp
    public ResponseEntity<String> sendDoctorAccessOTP( @RequestHeader(value = "Authorization", required = false) String token,
                                                       @RequestParam long mediId){
        ResponseEntity<String> validateToken = validateToken(token);
        if(!validateToken.getStatusCode().is2xxSuccessful()){
            return validateToken;
        }

        try{
            return ResponseEntity.ok(otpService.sendDoctorAccessOTP(mediId));
        }catch(RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @ApiOperation(value = "Verify the OTP for the doctor login")
    @PostMapping("/doctor-access")
    //verify the doctor login OTP
    public ResponseEntity<String> verifyDoctorAccessOTP( @RequestHeader(value = "Authorization", required = false) String token, @RequestParam long mediId, @RequestParam String otp){
        ResponseEntity<String> validateToken = validateToken(token);
        if(!validateToken.getStatusCode().is2xxSuccessful()){
            return validateToken;
        }

        Optional<Patient> patient = patientService.getUserByMediId(mediId);
        if(patient.isEmpty()){
            return ResponseEntity.badRequest().body("Invalid mediId or OTP");
        }

        return otpService.verifyOTP(mediId,otp)
                ? ResponseEntity.ok("OTP Verified")         //If verifyOTP() returns true
                : ResponseEntity.badRequest().body("Invalid OTP");

    }
    
}
