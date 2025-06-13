package com.sdgp.MediPass.service;

import com.sdgp.MediPass.model.Patient;
import com.sdgp.MediPass.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OTPService {
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private PatientRepository patientRepository;

    //to store the generated OTP temporarily in key-value pairs to verify the OTP (the email is stored as the key)
    private final Map<Long, String> otpStorage = new HashMap<>();

    private final Map<Long, String> doctorOtpMap = new ConcurrentHashMap<>(); //temporary storage of Doctor access OTP
    private final Map<Long, Boolean> doctorOtpVerifiedMap = new ConcurrentHashMap<>(); //to track Doctor OTP status of relevant mediIds

    //generate the otp
    public String generateOTP(){
        SecureRandom random = new SecureRandom();
        int otp = random.nextInt(9999);     //generates a random integer between 0 and 9999(inclusive)
        return String.format("%04d",otp);    //"%04d" ensures the generated number is always 4 digits
    }

    //find the relevant email through nic and mediId
    public String sendOTP(String nic, long mediId){
        Optional<Patient> patientOptional = patientRepository.findByNicAndMediId(nic, mediId);
        if(patientOptional.isEmpty()){
            throw new RuntimeException("Invalid NIC or MediID");
        }
        String email = patientOptional.get().getEmail();
        String otp = generateOTP();
        otpStorage.put(mediId, otp);   //stores otp temporarily and send it via email

        sendEmail(email, otp);
        return "OTP sent successfully to the mail relevant for the mediId "+ mediId;
    }

    //verify the otp
    public boolean verifyOTP(long mediId, String otp){
        String validOTP = otpStorage.get(mediId);
        if(validOTP != null && validOTP.equals(otp)){
            otpStorage.remove(mediId);  //remove OTP after use
            return true;
        }
        return false;
//        //checks whether map contains an OTP for the given email AND whether the given OTP matches with the one stored in the map
//        boolean isValid = otpStorage.containsKey(mediId) && otpStorage.get(mediId).equals(otp);
//        if(isValid){
//            otpStorage.remove(mediId);  // Remove OTP after verification to avoid reuse
//        }
//        return isValid;
    }

    //Send OTP to authorize doctor login access
    public String sendDoctorAccessOTP(Long mediId){
        Optional<Patient> patientOptional = patientRepository.findByMediId(mediId);
        if(patientOptional.isEmpty()){
            throw new RuntimeException("Invalid MediID");
        }
        String email = patientOptional.get().getEmail();
        String otp = generateOTP();
        otpStorage.put(mediId, otp);

        doctorOtpMap.put(mediId, otp);
        doctorOtpVerifiedMap.put(mediId, false); // Reset access each time a new OTP is sent

        sendDoctorAccessEmail(email, otp);
        return "OTP sent to the email revelant for the mediId "+ mediId;
    }

    //Verify OTP entered by the Doctor
    public boolean verifyDoctorAccessOTP(long mediId, String otp){
        String validOTP = doctorOtpMap.get(mediId);
        if(validOTP != null && validOTP.equals(otp)){
            doctorOtpVerifiedMap.put(mediId, true);     //change otp status
            doctorOtpMap.remove( mediId);   //remove otp after verification
            return true;
        }
        return false;
    }

    //send the email the to the relevant patient
    private void sendEmail(String email, String otp){
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email);
        msg.setSubject("Forgot password verification");
        msg.setText(" We have received a request to reset your MediPass account password."
                + " Use the following OTP code to reset your password. We advice you to not share this OTP with anyone.\n\n"
                + "Your OTP code is: "+otp+"\n\nMediPass \nSmart Records Smarter Care.");
        mailSender.send(msg);
    }

    private void sendDoctorAccessEmail(String email, String otp){
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email);
        msg.setSubject("Authorize Doctor Access");
        msg.setText(" We have received a request to allow access to view your MediPass account by a medical professional. They will have access to your medical records util they exit the session."
                + " If you want to allow access use the following OTP code. We advice you to not share this OTP with anyone.\n\n"
                + "Your OTP code is: "+otp+"\n\nMediPass \nSmart Records Smarter Care.");
        mailSender.send(msg);
    }

}
