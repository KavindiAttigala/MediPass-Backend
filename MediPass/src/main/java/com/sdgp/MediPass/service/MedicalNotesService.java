package com.sdgp.MediPass.service;

import com.sdgp.MediPass.model.GuestDoctor;
import com.sdgp.MediPass.model.MedicalNotes;
import com.sdgp.MediPass.model.Patient;
import com.sdgp.MediPass.repository.GuestDoctorRepository;
import com.sdgp.MediPass.repository.MedicalNotesRepo;
import com.sdgp.MediPass.repository.PatientRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

@Service
public class MedicalNotesService {
    @Autowired
    private MedicalNotesRepo notesRepo;

    @Autowired
    private PatientRepository patientRepo;

    @Autowired
    private GuestDoctorRepository guestDoctorRepo;

    @Transactional //to ensure the session remains open when accessing the collection
    public MedicalNotes saveNotes(Long mediId, String textContent) throws IOException {

        Optional<Patient> patientOptional = patientRepo.findByMediId(mediId);
        //when sending the notes to the DB incase the mediId is not found > login again
        if(patientOptional.isEmpty()){
            throw new IllegalArgumentException("Patient with mediId "+ mediId+" not found.");
        }

        // Fetch the most recent guest doctor entry
        Optional<GuestDoctor> guestDoctorOptional = guestDoctorRepo.findTopByOrderByDocIdDesc();
        if (guestDoctorOptional.isEmpty()) {
            throw new IllegalArgumentException("No Doctor information found for this session.");
        }

        GuestDoctor guestDoctor = guestDoctorOptional.get();

        MedicalNotes note = new MedicalNotes();
        note.setPatient(patientOptional.get());
        note.setGuestDoctor(guestDoctor);
        note.setDate(LocalDate.now());
        note.setTextContent(textContent);

        return notesRepo.save(note);
    }

    public List<MedicalNotes> getNotes(Long mediId) {
        // Fetch the patient by mediId from the repository
        Optional<Patient> patientOptional = patientRepo.findByMediId(mediId);
        if (patientOptional.isEmpty()) {
            throw new IllegalArgumentException("Patient with mediId " + mediId + " not found.");
        }

        // Fetch all medical notes for the given patient
        List<MedicalNotes> notes = notesRepo.findByPatientMediId(mediId);

        return notes;
    }
}
