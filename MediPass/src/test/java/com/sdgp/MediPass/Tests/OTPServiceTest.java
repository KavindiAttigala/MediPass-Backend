package com.sdgp.MediPass.Tests;
import com.sdgp.MediPass.model.Patient;
import com.sdgp.MediPass.repository.PatientRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.sdgp.MediPass.service.OTPService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.List;

public class OTPServiceTest {

    @InjectMocks
    private OTPService otpService;

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private PatientRepository patientRepository;

    private Patient patient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        patient = new Patient();
        patient.setMediId(927427L);
        patient.setNic("200378655644");
        patient.setEmail("slochanaw03@gmail.com");
    }

    @Test
    void testSendOTP_Success() {
        when(patientRepository.findByNicAndMediId("200378655644", 927427L)).thenReturn(Optional.of(patient));

        String result = otpService.sendOTP("200378655644", 927427L);

        assertTrue(result.contains("OTP sent successfully"));
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void testSendOTP_InvalidDetails() {
        when(patientRepository.findByNicAndMediId("wrongNIC", 927427L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> otpService.sendOTP("wrongNIC", 927427L));
    }

    @Test
    void testVerifyOTP_Success() {
        when(patientRepository.findByNicAndMediId("200378655644", 927427L)).thenReturn(Optional.of(patient));
        otpService.sendOTP("200378655644", 927427L); // stores OTP

        String otp = getOtpFromStorage(otpService, 927427L); // get OTP from map
        assertTrue(otpService.verifyOTP(927427L, otp));
    }

    @Test
    void testVerifyOTP_Failure() {
        assertFalse(otpService.verifyOTP(927427L, "0000"));
    }

    @Test
    void testSendDoctorAccessOTP_Success() {
        when(patientRepository.findByMediId(927427L)).thenReturn(Optional.of(patient));

        String result = otpService.sendDoctorAccessOTP(927427L);

        assertTrue(result.contains("OTP sent to the email"));
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void testVerifyDoctorAccessOTP_Success() {
        when(patientRepository.findByMediId(927427L)).thenReturn(Optional.of(patient));
        otpService.sendDoctorAccessOTP(927427L);

        String otp = getOtpFromDoctorMap(otpService, 927427L);
        assertTrue(otpService.verifyDoctorAccessOTP(927427L, otp));
    }

    @Test
    void testVerifyDoctorAccessOTP_Failure() {
        assertFalse(otpService.verifyDoctorAccessOTP(927427L, "0000"));
    }

    // 👇 Utility method to get OTP from internal map using reflection (since the map is private)
    private String getOtpFromStorage(OTPService service, Long mediId) {
        try {
            var field = OTPService.class.getDeclaredField("otpStorage");
            field.setAccessible(true);
            @SuppressWarnings("unchecked")
            var map = (Map<Long, String>) field.get(service);
            return map.get(mediId);
        } catch (Exception e) {
            return null;
        }
    }

    private String getOtpFromDoctorMap(OTPService service, Long mediId) {
        try {
            var field = OTPService.class.getDeclaredField("doctorOtpMap");
            field.setAccessible(true);
            @SuppressWarnings("unchecked")
            var map = (Map<Long, String>) field.get(service);
            return map.get(mediId);
        } catch (Exception e) {
            return null;
        }
    }
}
