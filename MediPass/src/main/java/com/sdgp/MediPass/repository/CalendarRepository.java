package com.sdgp.MediPass.repository;

import com.sdgp.MediPass.model.CalendarReminder;
import com.sdgp.MediPass.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CalendarRepository extends JpaRepository<CalendarReminder, Long> {
    List<CalendarReminder> findByPatient(Patient patient);

    List<CalendarReminder> findByMediID(Long mediId);
}
