package com.sdgp.MediPass.service;

import com.sdgp.MediPass.model.GuestDoctor;
import com.sdgp.MediPass.model.MedicalNotes;
import com.sdgp.MediPass.model.Patient;
import com.sdgp.MediPass.repository.GuestDoctorRepository;
import com.sdgp.MediPass.repository.MedicalNotesRepo;
import com.sdgp.MediPass.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MedicalNotesService {
    @Autowired
    private MedicalNotesRepo notesRepo;

    @Autowired
    private PatientRepository patientRepo;

    @Autowired
    private GuestDoctorRepository guestDoctorRepo;

    private final String uploadDirectory = "medipass/medicalNotes/";

    public MedicalNotes saveNotes(Long id, String textContent, MultipartFile file) throws IOException {

        Optional<Patient> patientOptional = patientRepo.findById(id);
        //when sending the notes to the DB incase the mediId is not found > login again
        if(patientOptional.isEmpty()){
            throw new IllegalArgumentException("Patient with mediId "+ id+" not found.");
        }

        Optional<GuestDoctor> guestDoctorOptional = guestDoctorRepo.findById(id);

        MedicalNotes note = new MedicalNotes();
        note.setPatient(patientOptional.get());
        note.setGuestDoctor(guestDoctorOptional.get());
        note.setDate(LocalDate.now());
        note.setTextContent(textContent);

        if(file != null && !file.isEmpty()){
            String fileName = UUID.randomUUID()+"_"+file.getOriginalFilename();         //generates a unique file name to prevent overwriting existing files.
            Path filePath = Paths.get(uploadDirectory+ fileName);
            Files.createDirectories(filePath.getParent());      //Ensures the uploads directory exists before saving the file and create a new folder if it doesn't exist
            Files.write(filePath, file.getBytes(), StandardOpenOption.CREATE);

            note.setFileName(file.getOriginalFilename());
            note.setFileType(file.getContentType());
            note.setFilePath(filePath.toString());

        }

        return notesRepo.save(note);

    }

    public List<MedicalNotes> getNotes(Long mediId){
        Optional<Patient> patientOptional = patientRepo.findById(mediId);
        if(patientOptional.isEmpty()){
            throw new IllegalArgumentException("Patient with mediId " + mediId + " not found.");
        }
        return notesRepo.findByPatient(patientOptional.get());
    }
}
