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

    public BloodDonationRecords saveBDRecords(long mediId,BloodDonationRecords bloodDonationRec){
        bloodDonationRec.setMediId(mediId);
        return bloodDonationRepository.save(bloodDonationRec);
    }

    public List<BloodDonationRecords> getAllDonations(long mediId){
        return bloodDonationRepository.findAllByMediId(mediId);
    }

}
