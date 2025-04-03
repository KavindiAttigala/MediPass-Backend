package com.sdgp.MediPass.controller;

import com.sdgp.MediPass.model.GuestDoctor;
import com.sdgp.MediPass.model.Patient;
import com.sdgp.MediPass.service.GuestDoctorService;
import com.sdgp.MediPass.service.OTPService;
import com.sdgp.MediPass.service.PatientService;
import com.sdgp.MediPass.util.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/medipass/guest-logging")
@Api(value="Guest Doctor Login", description = "Storing guest login information")
public class GuestDoctorController {
    @Autowired
    private GuestDoctorService guestDoctorService;
    @Autowired
    private PatientService patientService;
    @Autowired
    private OTPService otpService;
    @Autowired
    private JwtUtil jwtUtil;

    @ApiOperation(value = "Storing guest doctor login info in the DB")
    @PostMapping("/access")
    public ResponseEntity<?> doctorAccess(@RequestHeader(value = "Authorization", required = false) String token,
                                               @RequestParam long mediId, @RequestParam String otp, @RequestBody GuestDoctor guestDoctor){
        //validate the format of the token
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid token");
        }

        String jwtToken = token.substring(7);     // Extract the token part
        String extractedMediId = jwtUtil.extractMediId(jwtToken);   // Validate the token and extract mediId from it

        //validate and extract mediId from the token
        if (extractedMediId == null || !extractedMediId.equals(String.valueOf(mediId))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        }

        Optional<Patient> patientOptional =patientService.getPatientByMediId(mediId);
        if(patientOptional.isEmpty()){
            return ResponseEntity.badRequest().body("Invalid MediID");
        }

        //Link guestDoctor to Patient
        Patient patient = patientOptional.get();
        guestDoctor.setPatient(patient);

        //verify OTP access
        if(!otpService.verifyOTP(mediId,otp)){
            return ResponseEntity.badRequest().body("Invalid OTP");
        }

        //each guest doctor session is stored before adding medical notes
        GuestDoctor savedDoctor = guestDoctorService.saveDoctorDetails(mediId, guestDoctor);
        return ResponseEntity.ok(savedDoctor);  //Returns JSON object instead of String
    }
}
