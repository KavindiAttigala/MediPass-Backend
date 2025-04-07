package com.sdgp.MediPass.Tests;

import com.sdgp.MediPass.model.Patient;
import com.sdgp.MediPass.model.VaccinationRecords;
import com.sdgp.MediPass.repository.PatientRepository;
import com.sdgp.MediPass.repository.VaccinationRecordRepository;
import com.sdgp.MediPass.service.VaccinationRecordService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VaccinationRecordServiceTest {

    @Mock
    private VaccinationRecordRepository vaccinationRecordRepository;

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private VaccinationRecordService vaccinationRecordService;

    private VaccinationRecords vaccinationRecord;
    private Patient patient;

    @BeforeEach
    void setup() {
        patient = new Patient();
        patient.setMediId(1001L);

        vaccinationRecord = new VaccinationRecords();
        vaccinationRecord.setMediId(1001L);
        vaccinationRecord.setPatient(patient);
    }

    @Test
    void testAddVaccinationRecords_Success() {
        when(patientRepository.findByMediId(1001L)).thenReturn(Optional.of(patient));
        when(vaccinationRecordRepository.save(any(VaccinationRecords.class))).thenReturn(vaccinationRecord);

        VaccinationRecords result = vaccinationRecordService.addVaccinationRecords(1001L, vaccinationRecord);

        assertNotNull(result);
        assertEquals(1001L, result.getMediId());
        verify(vaccinationRecordRepository, times(1)).save(vaccinationRecord);
    }

    @Test
    void testAddVaccinationRecords_PatientNotFound() {
        long mediId = 9999L;
        when(patientRepository.findByMediId(mediId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            vaccinationRecordService.addVaccinationRecords(mediId, vaccinationRecord);
        });

        verify(vaccinationRecordRepository, never()).save(any());
    }

    @Test
    void testGetAllVaccinationRecords() {
        long mediId = 1001L;
        List<VaccinationRecords> list = Arrays.asList(vaccinationRecord, new VaccinationRecords());
        when(vaccinationRecordRepository.findByMediId(mediId)).thenReturn(list);

        List<VaccinationRecords> result = vaccinationRecordService.getAllVaccinationRecords(mediId);

        assertEquals(2, result.size());
        verify(vaccinationRecordRepository, times(1)).findByMediId(mediId);
    }
}
