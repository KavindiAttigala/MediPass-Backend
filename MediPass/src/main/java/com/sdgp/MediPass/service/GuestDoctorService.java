package com.sdgp.MediPass.service;

import com.sdgp.MediPass.model.GuestDoctor;
import com.sdgp.MediPass.repository.GuestDoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GuestDoctorService {
    @Autowired
    private GuestDoctorRepository guestDoctorRepo;

    public GuestDoctor saveDoctor(GuestDoctor guestDoctor){
        return guestDoctorRepo.save(guestDoctor);
    }
}
