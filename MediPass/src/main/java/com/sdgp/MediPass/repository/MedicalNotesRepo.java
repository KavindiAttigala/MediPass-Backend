package com.sdgp.MediPass.repository;

import com.sdgp.MediPass.model.MedicalNotes;
import com.sdgp.MediPass.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicalNotesRepo extends JpaRepository<MedicalNotes, Long> {
    List<MedicalNotes> findByMediId(Patient patient);
}
