package com.sdgp.MediPass.Tests;

import com.sdgp.MediPass.model.ChronicDisease;
import com.sdgp.MediPass.model.Patient;
import com.sdgp.MediPass.repository.ChronicDiseaseRepository;
import com.sdgp.MediPass.repository.PatientRepository;
import com.sdgp.MediPass.service.ChronicDiseaseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ChronicDiseaseServiceTest {

    @Mock
    private ChronicDiseaseRepository chroDiseRepo;

    @Mock
    private PatientRepository patientRepo;

    @InjectMocks
    private ChronicDiseaseService chronicDiseaseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddDiseaseRecords_success() throws IOException {
        // Given
        long mediId = 1L;
        String diseaseName = "Diabetes";
        String medication = "Metformin";
        int dosage = 500;
        LocalDate start = LocalDate.of(2024, 1, 1);
        LocalDate end = LocalDate.of(2024, 12, 31);

        Patient patient = new Patient();
        patient.setMediId(mediId);
        // Setup the patient repository to return the patient
        when(patientRepo.findById(mediId)).thenReturn(Optional.of(patient));

        // Create a chronic disease object that would be returned after saving
        ChronicDisease savedDisease = new ChronicDisease();
        savedDisease.setDiseaseName(diseaseName);
        savedDisease.setMedication(medication);
        savedDisease.setDosage(dosage);
        savedDisease.setStartDate(start);
        savedDisease.setEndDate(end);
        savedDisease.setPatient(patient);

        when(chroDiseRepo.save(any(ChronicDisease.class))).thenReturn(savedDisease);

        // When
        ChronicDisease result = chronicDiseaseService.addDiseaseRecords(diseaseName, mediId, medication, dosage, start, end);

        // Then
        assertNotNull(result);
        assertEquals(diseaseName, result.getDiseaseName());
        assertEquals(medication, result.getMedication());
        assertEquals(dosage, result.getDosage());
        assertEquals(start, result.getStartDate());
        assertEquals(end, result.getEndDate());
        assertEquals(patient, result.getPatient());

        // Verify that repositories were called correctly
        verify(patientRepo, times(1)).findById(mediId);
        verify(chroDiseRepo, times(1)).save(any(ChronicDisease.class));
    }

    @Test
    void testAddDiseaseRecords_patientNotFound() {
        // Given
        long mediId = 1L;
        when(patientRepo.findById(mediId)).thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            chronicDiseaseService.addDiseaseRecords("Diabetes", mediId, "Metformin", 500,
                    LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31));
        });
        assertTrue(exception.getMessage().contains("Patient with mediId " + mediId + " not found"));
        verify(patientRepo, times(1)).findById(mediId);
        verify(chroDiseRepo, never()).save(any(ChronicDisease.class));
    }

    @Test
    void testGetDisease_success() {
        // Given
        long mediId = 1L;
        Patient patient = new Patient();
        patient.setMediId(mediId);

        ChronicDisease disease1 = new ChronicDisease();
        disease1.setDiseaseName("Diabetes");
        disease1.setPatient(patient);

        ChronicDisease disease2 = new ChronicDisease();
        disease2.setDiseaseName("Hypertension");
        disease2.setPatient(patient);

        List<ChronicDisease> diseaseList = Arrays.asList(disease1, disease2);

        when(patientRepo.findById(mediId)).thenReturn(Optional.of(patient));
        when(chroDiseRepo.findByPatient(patient)).thenReturn(diseaseList);

        // When
        List<ChronicDisease> result = chronicDiseaseService.getDisease(mediId);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Diabetes", result.get(0).getDiseaseName());
        assertEquals("Hypertension", result.get(1).getDiseaseName());
        verify(patientRepo, times(1)).findById(mediId);
        verify(chroDiseRepo, times(1)).findByPatient(patient);
    }

    @Test
    void testGetDisease_patientNotFound() {
        // Given
        long mediId = 1L;
        when(patientRepo.findById(mediId)).thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            chronicDiseaseService.getDisease(mediId);
        });
        assertTrue(exception.getMessage().contains("Patient with mediId " + mediId + " not found."));
        verify(patientRepo, times(1)).findById(mediId);
        verify(chroDiseRepo, never()).findByPatient(any());
    }
}