package com.sdgp.MediPass.service;

import com.sdgp.MediPass.model.BloodDonationRecords;
import com.sdgp.MediPass.repository.BloodDonationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BloodDonationService {

    @Autowired
    private BloodDonationRepository bloodDonationRepository;

    public BloodDonationRecords saveBDRecords(BloodDonationRecords bloodDonationRec){
        return bloodDonationRepository.save(bloodDonationRec);
    }

    public List<BloodDonationRecords> getAllDonations(){
        return bloodDonationRepository.findAll();
    }

}
