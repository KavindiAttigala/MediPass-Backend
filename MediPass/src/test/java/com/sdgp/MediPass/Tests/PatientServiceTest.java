package com.sdgp.MediPass.Tests;

import com.sdgp.MediPass.model.Patient;
import com.sdgp.MediPass.repository.PatientRepository;
import com.sdgp.MediPass.service.PatientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PatientServiceTest {

    @InjectMocks
    private PatientService patientService;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JavaMailSender mailSender;

    private Patient patient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        patient = new Patient();
        patient.setMediId(927427);
        patient.setNic("200378655644");
        patient.setEmail("slochanaw03@gmail.com");
        patient.setPassword("Lochana123");
        patient.setFirstName("Lochi");
        patient.setLastName("Wijesinghe");
        patient.setRole("Adult");
    }

    @Test
    void testRegisterPatientAdult_Success() {
        when(patientRepository.findByNic(anyString())).thenReturn(Collections.emptyList());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(patientRepository.save(any(Patient.class))).thenReturn(patient);

        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        Patient result = patientService.registerPatientAdult(patient);

        assertNotNull(result);
        assertEquals(patient.getEmail(), result.getEmail());
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void testRegisterPatientAdult_AlreadyExists() {
        when(patientRepository.findByNic(anyString())).thenReturn(List.of(patient));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            patientService.registerPatientAdult(patient);
        });

        assertEquals("Account already exists for this NIC", ex.getMessage());
    }

    @Test
    void testRegisterPatientChild_Success() {
        when(patientRepository.findByRoleAndNic("Adult", patient.getNic())).thenReturn(List.of(patient));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(patientRepository.save(any(Patient.class))).thenReturn(patient);

        Patient result = patientService.registerPatientChild(patient);

        assertNotNull(result);
        assertEquals(patient.getEmail(), result.getEmail());
    }

    @Test
    void testRegisterPatientChild_NoAdultAccount() {
        when(patientRepository.findByRoleAndNic("Adult", patient.getNic())).thenReturn(Collections.emptyList());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            patientService.registerPatientChild(patient);
        });

        assertEquals("No account found for this NIC", ex.getMessage());
    }

    @Test
    void testLogin_Success() {
        patient.setPassword("encodedPassword");
        when(patientRepository.findByMediId(927247L)).thenReturn(Optional.of(patient));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        List<String> tokens = patientService.login(927247L, "rawPassword");

        assertFalse(tokens.isEmpty());
    }

    @Test
    void testLogin_Failure() {
        when(patientRepository.findByMediId(927247L)).thenReturn(Optional.empty());

        List<String> tokens = patientService.login(927247L, "wrong");

        assertTrue(tokens.isEmpty());
    }

    @Test
    void testGetPatientByMediId() {
        when(patientRepository.findByMediId(927247)).thenReturn(Optional.of(patient));

        Optional<Patient> result = patientService.getPatientByMediId(927247L);

        assertTrue(result.isPresent());
    }

    @Test
    void testUpdatePatient_Success() {
        Patient updated = new Patient();
        updated.setFirstName("Updated");
        updated.setHeight(180.0);

        when(patientRepository.findById(927247L)).thenReturn(Optional.of(patient));
        when(patientRepository.save(any(Patient.class))).thenReturn(patient);

        Patient result = patientService.updatePatient(927247L, updated);

        assertNotNull(result);
        verify(patientRepository).save(any(Patient.class));
    }

    @Test
    void testDeletePatient() {
        doNothing().when(patientRepository).deleteById(927247L);

        patientService.deletePatient(927247L);

        verify(patientRepository, times(1)).deleteById(927247L);
    }

    @Test
    void testVerifyUserAndGetEmail_Found() {
        when(patientRepository.findByNicAndMediId(anyString(), anyLong())).thenReturn(Optional.of(patient));

        String email = patientService.verifyUserAndGetEmail("123456789V", 927247L);

        assertEquals("test@example.com", email);
    }

    @Test
    void testVerifyUserAndGetEmail_NotFound() {
        when(patientRepository.findByNicAndMediId(anyString(), anyLong())).thenReturn(Optional.empty());

        String email = patientService.verifyUserAndGetEmail("123456789V", 927247L);

        assertEquals("User email not found", email);
    }

    @Test
    void testUpdatePassword_Success() {
        when(patientRepository.findByMediId(927247L)).thenReturn(Optional.of(patient));
        when(passwordEncoder.encode(anyString())).thenReturn("newEncoded");

        boolean result = patientService.updatePassword(927247L, "newPassword");

        assertTrue(result);
        verify(patientRepository).save(any(Patient.class));
    }

    @Test
    void testVerifyOldPassword_Match() {
        when(patientRepository.findByMediId(927247L)).thenReturn(Optional.of(patient));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        boolean match = patientService.verifyOldPassword(927247L, "oldPass");

        assertTrue(match);
    }

    @Test
    void testChangePassword_Success() {
        when(patientRepository.findByMediId(927247L)).thenReturn(Optional.of(patient));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedNew");

        boolean result = patientService.changePassword(927247L, "old", "new");

        assertTrue(result);
    }

    @Test
    void testChangePassword_Failure_NoPatient() {
        when(patientRepository.findByMediId(927247L)).thenReturn(Optional.empty());

        boolean result = patientService.changePassword(927247L, "old", "new");

        assertFalse(result);
    }

    @Test
    void testChangePassword_Failure_WrongOldPassword() {
        when(patientRepository.findByMediId(927247L)).thenReturn(Optional.of(patient));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        boolean result = patientService.changePassword(927247L, "old", "new");

        assertFalse(result);
    }
}

