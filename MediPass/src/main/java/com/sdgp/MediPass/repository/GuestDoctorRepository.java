package com.sdgp.MediPass.repository;

import com.sdgp.MediPass.model.GuestDoctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GuestDoctorRepository extends JpaRepository<GuestDoctor, Long> {
    // retrieve the last guest doctor entry
    Optional<GuestDoctor> findTopByOrderByDocIdDesc();
}
