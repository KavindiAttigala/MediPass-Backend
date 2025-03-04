package com.sdgp.MediPass.repository;

import com.sdgp.MediPass.model.GuestDoctor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuestDoctorRepository extends JpaRepository<GuestDoctor, Long> {
}
