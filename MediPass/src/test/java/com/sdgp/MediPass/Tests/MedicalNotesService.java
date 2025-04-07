package com.sdgp.MediPass.Tests;

import com.sdgp.MediPass.model.GuestDoctor;
import com.sdgp.MediPass.model.MedicalNotes;
import com.sdgp.MediPass.model.Patient;
import com.sdgp.MediPass.repository.GuestDoctorRepository;
import com.sdgp.MediPass.repository.MedicalNotesRepo;
import com.sdgp.MediPass.repository.PatientRepository;
import com.sdgp.MediPass.service.MedicalNotesService;
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

class MedicalNotesServiceTest {

    @Mock
    private MedicalNotesRepo notesRepo;

    @Mock
    private PatientRepository patientRepo;

    @Mock
    private GuestDoctorRepository guestDoctorRepo;

    @InjectMocks
    private MedicalNotesService medicalNotesService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveNotesSuccess() throws IOException {
        // Given
        Long mediId = 1L;
        String textContent = "Patient is recovering well.";
        Patient patient = new Patient();
        patient.setMediId(mediId);

        GuestDoctor guestDoctor = new GuestDoctor();
        // Assuming docId is set by the repository (or any unique identifier)
        // You can set additional fields if needed.
        guestDoctor.setDocId(100L);

        // Set up mocks for patient and guestDoctor retrieval
        when(patientRepo.findByMediId(mediId)).thenReturn(Optional.of(patient));
        when(guestDoctorRepo.findTopByOrderByDocIdDesc()).thenReturn(Optional.of(guestDoctor));

        // Prepare the MedicalNotes object that the repository will return after saving
        MedicalNotes savedNote = new MedicalNotes();
        savedNote.setPatient(patient);
        savedNote.setGuestDoctor(guestDoctor);
        savedNote.setDate(LocalDate.now());
        savedNote.setTextContent(textContent);

        when(notesRepo.save(any(MedicalNotes.class))).thenReturn(savedNote);

        // When
        MedicalNotes result = medicalNotesService.saveNotes(mediId, textContent);

        // Then
        assertNotNull(result);
        assertEquals(textContent, result.getTextContent());
        assertEquals(patient, result.getPatient());
        assertEquals(guestDoctor, result.getGuestDoctor());
        assertEquals(LocalDate.now(), result.getDate());

        verify(patientRepo, times(1)).findByMediId(mediId);
        verify(guestDoctorRepo, times(1)).findTopByOrderByDocIdDesc();
        verify(notesRepo, times(1)).save(any(MedicalNotes.class));
    }

    @Test
    void testSaveNotesPatientNotFound() {
        // Given
        Long mediId = 1L;
        String textContent = "Test note";

        when(patientRepo.findByMediId(mediId)).thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            medicalNotesService.saveNotes(mediId, textContent);
        });
        assertTrue(exception.getMessage().contains("Patient with mediId " + mediId + " not found."));
        verify(patientRepo, times(1)).findByMediId(mediId);
        verify(guestDoctorRepo, never()).findTopByOrderByDocIdDesc();
        verify(notesRepo, never()).save(any(MedicalNotes.class));
    }

    @Test
    void testSaveNotesGuestDoctorNotFound() {
        // Given
        Long mediId = 1L;
        String textContent = "Test note";
        Patient patient = new Patient();
        patient.setMediId(mediId);

        when(patientRepo.findByMediId(mediId)).thenReturn(Optional.of(patient));
        when(guestDoctorRepo.findTopByOrderByDocIdDesc()).thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            medicalNotesService.saveNotes(mediId, textContent);
        });
        assertTrue(exception.getMessage().contains("No Doctor information found"));
        verify(patientRepo, times(1)).findByMediId(mediId);
        verify(guestDoctorRepo, times(1)).findTopByOrderByDocIdDesc();
        verify(notesRepo, never()).save(any(MedicalNotes.class));
    }

    @Test
    void testGetNotesSuccess() {
        // Given
        Long mediId = 1L;
        Patient patient = new Patient();
        patient.setMediId(mediId);

        MedicalNotes note1 = new MedicalNotes();
        note1.setTextContent("Note 1");
        note1.setPatient(patient);

        MedicalNotes note2 = new MedicalNotes();
        note2.setTextContent("Note 2");
        note2.setPatient(patient);

        List<MedicalNotes> notesList = Arrays.asList(note1, note2);

        when(patientRepo.findByMediId(mediId)).thenReturn(Optional.of(patient));
        when(notesRepo.findByPatient_MediId(mediId)).thenReturn(notesList);

        // When
        List<MedicalNotes> result = medicalNotesService.getNotes(mediId);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Note 1", result.get(0).getTextContent());
        assertEquals("Note 2", result.get(1).getTextContent());

        verify(patientRepo, times(1)).findByMediId(mediId);
        verify(notesRepo, times(1)).findByPatient_MediId(mediId);
    }

    @Test
    void testGetNotesPatientNotFound() {
        // Given
        Long mediId = 1L;
        when(patientRepo.findByMediId(mediId)).thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            medicalNotesService.getNotes(mediId);
        });
        assertTrue(exception.getMessage().contains("Patient with mediId " + mediId + " not found."));
        verify(patientRepo, times(1)).findByMediId(mediId);
        verify(notesRepo, never()).findByPatient_MediId(mediId);
    }
}
