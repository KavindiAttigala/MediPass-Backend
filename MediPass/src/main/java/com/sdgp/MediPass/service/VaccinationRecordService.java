package com.sdgp.MediPass.service;

import com.sdgp.MediPass.model.VaccinationRecords;
import com.sdgp.MediPass.repository.VaccinationRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VaccinationRecordService {
    @Autowired
    private VaccinationRecordRepository vaccinationRecordRepository;

    public VaccinationRecords addVaccinationRecords(VaccinationRecords vaccinationRecords){
        return vaccinationRecordRepository.save(vaccinationRecords);
    }

    public List<VaccinationRecords> getAllVaccinationRecords(){
        return vaccinationRecordRepository.findAll();
    }

}
