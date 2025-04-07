package com.sdgp.MediPass.Tests;

import com.sdgp.MediPass.model.BloodDonationRecords;
import com.sdgp.MediPass.service.BloodDonationService;
import com.sdgp.MediPass.model.Patient;
import com.sdgp.MediPass.repository.BloodDonationRepository;
import com.sdgp.MediPass.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BloodDonationServiceTest {

    @Mock
    private BloodDonationRepository bloodDonationRepository;

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private BloodDonationService bloodDonationService;

    private BloodDonationRecords donationRecord;
    private Patient patient;

    @BeforeEach
    void setup() {
        patient = new Patient();
        patient.setMediId(123L);

        donationRecord = new BloodDonationRecords();
        donationRecord.setMediId(123L);
        donationRecord.setPatient(patient);
    }

    @Test
    void testSaveBDRecords_Success() {
        long mediId = 1L;
        when(patientRepository.findByMediId(mediId)).thenReturn(Optional.of(patient));
        when(bloodDonationRepository.save(any(BloodDonationRecords.class))).thenReturn(donationRecord);

        BloodDonationRecords savedRecord = bloodDonationService.saveBDRecords(mediId, donationRecord);

        assertNotNull(savedRecord);
        assertEquals(1L, savedRecord.getMediId());
        verify(bloodDonationRepository, times(1)).save(donationRecord);
    }

    @Test
    void testSaveBDRecords_PatientNotFound() {
        long mediId = 1L;
        when(patientRepository.findByMediId(mediId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            bloodDonationService.saveBDRecords(mediId, donationRecord);
        });

        assertEquals("Patient with MediID: 999 not found", exception.getMessage());
        verify(bloodDonationRepository, never()).save(any());
    }

    @Test
    void testGetAllDonations() {
        long mediId=1L;
        List<BloodDonationRecords> recordList = Arrays.asList(donationRecord, new BloodDonationRecords());
        when(bloodDonationRepository.findAllByMediId(123L)).thenReturn(recordList);

        List<BloodDonationRecords> result = bloodDonationService.getAllDonations(mediId);

        assertEquals(2, result.size());

        verify(bloodDonationRepository, times(1)).findAllByMediId(mediId);
    }
}
