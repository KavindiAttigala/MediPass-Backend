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

@Service
public class OTPService {
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private PatientRepository patientRepository;

    //to store the generated OTP temporarily in key-value pairs to verify the OTP (the email is stored as the key)
    private final Map<String, String> otpStorage = new HashMap<>();

    //find the relevant email through nic and mediId
    public String sendOTP(String nic, long mediId){
        Optional<Patient> patientOptional = patientRepository.findByNicAndMediId(nic, mediId);
        if(patientOptional.isEmpty()){
            throw new RuntimeException("Invalid NIC or MediID");
        }
        String email = patientOptional.get().getEmail();
        String otp = generateOTP();
        otpStorage.put(email, otp);   //stores otp temporarily and send it via email

        sendEmail(email, otp);
        return "OTP sent successfully to the mail relevant for the mediId "+ mediId;
    }


    //Send OTP for doctor login access
    public String sendDoctorAccessOTP(Long mediId){
        List<Patient> patientOptional = patientRepository.findByMediId(mediId);
        if(patientOptional.isEmpty()){
            throw new RuntimeException("Invalid MediID");
        }
        String email = patientOptional.get(0).getEmail();
        String otp = generateOTP();
        otpStorage.put(email, otp);

        sendEmail(email, otp);
        return "OTP sent to the email revelant for the mediId "+ mediId;
    }


    //generate the otp
    public String generateOTP(){
        SecureRandom random = new SecureRandom();
        int otp = random.nextInt(9999);     //generates a random integer between 0 and 9999(inclusive)
        return String.format("%04d"+otp);    //"%04d" ensures the generated number is always 4 digits
    }

    //send the email the to the relevant patient
    private void sendEmail(String email, String otp){
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setSubject("Your OTP code");
        msg.setText("""
                We have received a request to reset your MediPass account password. Use the following OTP code to reset yoru password. We advice you to not share this OTP with anyone./n""");
        msg.setText("Your OTP code is: "+otp);
        mailSender.send(msg);
    }

    //verify the otp
    public boolean verifyOTP(String email, String otp){
        //checks whether map contains an OTP for the given email AND whether the given OTP matches with the one stored in the map
        return otpStorage.containsKey(email) && otpStorage.get(email).equals(otp);
    }
}
