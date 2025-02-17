package com.sdgp.MediPass.service;

import com.sdgp.MediPass.model.Patient;
import com.sdgp.MediPass.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Service
public class OTPService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private PatientRepository patientRepository;

    private final Map<String, String> otpStorage = new HashMap<>();

    public String sendOTP(String nic, String mediId) {
        Optional<Patient> patientOptional = patientRepository.findByNicAndMediId(nic, mediId);

        if (patientOptional.isEmpty()) {
            throw new RuntimeException("Invalid NIC or MediID");
        }

        String email = patientOptional.get().getEmail();
        String otp = generateOTP();
        otpStorage.put(email, otp);

        sendOTPEmail(email, otp);
        return "OTP sent successfully to the email relevant for your "+mediId;
    }

    public String generateOTP() {
        SecureRandom random = new SecureRandom();
        int otp = random.nextInt(9999);
        return String.format("%04d", otp);
    }

    private void sendOTPEmail(String email, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Password Reset OTP");
        message.setText("Use this OTP to reset your password: "+otp);
        mailSender.send(message);
    }

    public boolean verifyOTP(String email, String otp) {
        return otpStorage.containsKey(email) && otpStorage.get(email).equals(otp);
    }
}
