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
    PatientRepository patientRepository;

    public BloodDonationRecords saveBDRecords(long mediId,BloodDonationRecords bloodDonationRec){
        Optional<Patient> patient = patientRepository.findByMediId(mediId);
        bloodDonationRec.setMediId(mediId);
        bloodDonationRec.setPatient(patient);
        return bloodDonationRepository.save(bloodDonationRec);
    }

    public List<BloodDonationRecords> getAllDonations(long mediId){
        return bloodDonationRepository.findAllByMediId(mediId);
    }

}
