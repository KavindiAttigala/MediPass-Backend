package com.sdgp.MediPass.service;

import com.sdgp.MediPass.model.Patient;
import com.sdgp.MediPass.model.VaccinationRecords;
import com.sdgp.MediPass.repository.PatientRepository;
import com.sdgp.MediPass.repository.VaccinationRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VaccinationRecordService {
    @Autowired
    private VaccinationRecordRepository vaccinationRecordRepository;
    @Autowired
    private PatientRepository patientRepository;

    public VaccinationRecords addVaccinationRecords(long mediId, VaccinationRecords vaccinationRecords){
        Optional<Patient> patient = patientRepository.findByMediId(mediId);
        vaccinationRecords.setMediId(mediId);
        vaccinationRecords.setPatient(patient.get());
        return vaccinationRecordRepository.save(vaccinationRecords);

    }

    public List<VaccinationRecords> getAllVaccinationRecords(long mediId){
        return vaccinationRecordRepository.findByMediId(mediId);
    }

}
