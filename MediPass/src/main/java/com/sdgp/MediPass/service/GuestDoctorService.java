package com.sdgp.MediPass.service;

import com.sdgp.MediPass.model.GuestDoctor;
import com.sdgp.MediPass.repository.GuestDoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class GuestDoctorService {
    @Autowired
    private GuestDoctorRepository guestDoctorRepo;

    private OTPService otpService;

    //store doctor details temporarily
    private final Map<Long, Boolean> otpStatus = new HashMap<>();

    public boolean verifyOtpForMediId(long mediId, String otp) {
        boolean isValid = otpService.verifyOTP(mediId, otp);
        if (isValid) {
            otpStatus.put(mediId, true); // Mark as verified
        }
        return isValid;
    }

    public GuestDoctor saveDoctorDetails(long mediId, GuestDoctor guestDoctor) {
        if (!otpStatus.getOrDefault(mediId, false)) {
            throw new RuntimeException("OTP verification required before saving doctor details");
        }

        //Store doctor details in the database
        GuestDoctor savedDoctor = guestDoctorRepo.save(guestDoctor);
        otpStatus.remove(mediId);
        return savedDoctor;
    }

//    public GuestDoctor saveDoctor(GuestDoctor guestDoctor){
//        return guestDoctorRepo.save(guestDoctor);
//    }
}
