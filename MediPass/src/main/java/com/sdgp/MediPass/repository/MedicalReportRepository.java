package com.sdgp.MediPass.repository;

import com.sdgp.MediPass.enums.ReportType;
import com.sdgp.MediPass.model.MedicalReports;
import com.sdgp.MediPass.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MedicalReportRepository extends JpaRepository <MedicalReports, Long> {
    List<MedicalReports> findByPatientMediId (Long mediId);

    List<MedicalReports> findByReportType(ReportType reportType);
    List<MedicalReports> findByPatientMediIdAndReportType (Long mediId, ReportType reportType);

    @Query("SELECT r FROM MedicalReports r WHERE r.reportType = :reportType AND r.patient.MediId = :patientId")
    List<MedicalReports> filterReports(@Param("reportType") ReportType reportType, @Param("patientId") Long patientId);
}
