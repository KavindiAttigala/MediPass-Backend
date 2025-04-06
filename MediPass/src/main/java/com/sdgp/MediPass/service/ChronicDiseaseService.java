package com.sdgp.MediPass.service;

import com.sdgp.MediPass.model.ChronicDisease;
import com.sdgp.MediPass.model.Patient;
import com.sdgp.MediPass.repository.ChronicDiseaseRepository;
import com.sdgp.MediPass.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ChronicDiseaseService {
    private final ChronicDiseaseRepository chroDiseRepo;
    private final PatientRepository patientRepo;
    public ChronicDiseaseService(ChronicDiseaseRepository chroDiseRepo, PatientRepository patientRepo) {
        this.chroDiseRepo = chroDiseRepo;
        this.patientRepo = patientRepo;
    }

    public ChronicDisease addDiseaseRecords(String diseaseName ,long mediId, String medication, int dosage, LocalDate start, LocalDate end) throws IOException {
        Optional<Patient> patientOptional = patientRepo.findById(mediId);
        if (patientOptional.isEmpty()){
            throw new IllegalArgumentException("Patient with mediId "+mediId+" not found");
        }

        ChronicDisease chronicDisease = new ChronicDisease();
        chronicDisease.setDiseaseName(diseaseName);
        chronicDisease.setMedication(medication);
        chronicDisease.setDosage(dosage);
        chronicDisease.setStartDate(start);
        chronicDisease.setEndDate(end);
        chronicDisease.setPatient(patientOptional.get());

        return chroDiseRepo.save(chronicDisease);
    }

    public List<ChronicDisease> getDisease(long mediId){
        Optional<Patient> patientOptional = patientRepo.findById(mediId);
        if(patientOptional.isEmpty()){
            throw new IllegalArgumentException("Patient with mediId " + mediId + " not found.");
        }
        return chroDiseRepo.findByPatient(patientOptional.get());
    }

}
