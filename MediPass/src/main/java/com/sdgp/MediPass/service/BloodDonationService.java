package com.sdgp.MediPass.service;

import com.sdgp.MediPass.model.BloodDonationRecords;
import com.sdgp.MediPass.model.Patient;
import com.sdgp.MediPass.repository.BloodDonationRepository;
import com.sdgp.MediPass.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BloodDonationService {

    @Autowired
    private BloodDonationRepository bloodDonationRepository;

    @Autowired
    private PatientRepository patientRepository;

    public BloodDonationRecords saveBDRecords(long mediId,BloodDonationRecords bloodDonationRec){
        Optional<Patient> patient = patientRepository.findByMediId(mediId);
        if(patient.isPresent()){
            bloodDonationRec.setMediId(mediId);
            bloodDonationRec.setPatient(patient.get());
            return bloodDonationRepository.save(bloodDonationRec);
        }else{
            throw new RuntimeException("Patient with MediID: " + mediId + " not found");
        }
    }

    public List<BloodDonationRecords> getAllDonations(long mediId){
        return bloodDonationRepository.findAllByMediId(mediId);
    }

}
