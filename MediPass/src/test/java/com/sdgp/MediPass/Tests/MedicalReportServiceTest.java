package com.sdgp.MediPass.Tests;

import com.sdgp.MediPass.enums.ReportType;
import com.sdgp.MediPass.model.MedicalReports;
import com.sdgp.MediPass.model.Patient;
import com.sdgp.MediPass.repository.MedicalReportRepository;
import com.sdgp.MediPass.repository.PatientRepository;
import com.sdgp.MediPass.service.MedicalReportService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MedicalReportServiceTest {

    @Mock
    private MedicalReportRepository medicalReportRepository;

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private MedicalReportService medicalReportService;

    // Directory used by the service
    private final String uploadDir = "uploads/medical-reports/";

    // Keep track of files created during tests for cleanup
    private File createdFile;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        // Ensure the upload directory exists
        new File(uploadDir).mkdirs();
    }

    @AfterEach
    void tearDown() throws Exception {
        // Clean up created file if it exists
        if (createdFile != null && createdFile.exists()) {
            createdFile.delete();
        }
        // Optionally, clean up the uploads directory if empty.
        File dir = new File(uploadDir);
        if (dir.exists() && dir.isDirectory() && dir.listFiles().length == 0) {
            dir.delete();
        }
        closeable.close();
    }

    @Test
    void testSaveReport_success() throws IOException {
        // Given: a mock MultipartFile with dummy content
        String originalFilename = "testReport.pdf";
        byte[] content = "dummy report content".getBytes();
        MultipartFile multipartFile = new MockMultipartFile("file", originalFilename, "application/pdf", content);

        String title = "Test Report";
        String description = "This is a test report.";
        // Replace 'LAB' with a valid constant from your ReportType enum
        ReportType reportType = ReportType.LAB;
        Long patientId = 1L;

        Patient patient = new Patient();
        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));

        // When saving, the repository should return the report
        when(medicalReportRepository.save(any(MedicalReports.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // When
        MedicalReports savedReport = medicalReportService.saveReport(multipartFile, title, description, reportType, patientId);

        // Then
        assertNotNull(savedReport);
        assertEquals(title, savedReport.getTitle());
        assertEquals(description, savedReport.getDescription());
        assertEquals(reportType, savedReport.getReportType());
        assertEquals(patient, savedReport.getPatient());
        assertNotNull(savedReport.getFileUrl());

        // Verify that file was actually written
        createdFile = new File(savedReport.getFileUrl());
        assertTrue(createdFile.exists());
        byte[] fileContent = Files.readAllBytes(createdFile.toPath());
        assertArrayEquals(content, fileContent);

        verify(patientRepository, times(1)).findById(patientId);
        verify(medicalReportRepository, times(1)).save(any(MedicalReports.class));
    }

    @Test
    void testSaveReport_patientNotFound() {
        // Given
        String originalFilename = "report.pdf";
        byte[] content = "dummy content".getBytes();
        MultipartFile multipartFile = new MockMultipartFile("file", originalFilename, "application/pdf", content);
        String title = "Title";
        String description = "Description";
        // Replace 'LAB' with a valid constant from your ReportType enum
        ReportType reportType = ReportType.LAB;
        Long patientId = 99L;

        when(patientRepository.findById(patientId)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                medicalReportService.saveReport(multipartFile, title, description, reportType, patientId)
        );
        assertTrue(exception.getMessage().contains("Patient not found"));
        verify(patientRepository, times(1)).findById(patientId);
        verify(medicalReportRepository, never()).save(any());
    }

    @Test
    void testGetAllReports() {
        // Given
        MedicalReports report1 = new MedicalReports();
        report1.setTitle("Report1");
        MedicalReports report2 = new MedicalReports();
        report2.setTitle("Report2");

        List<MedicalReports> reports = Arrays.asList(report1, report2);
        when(medicalReportRepository.findAll()).thenReturn(reports);

        // When
        List<MedicalReports> result = medicalReportService.getAllReports();

        // Then
        assertEquals(2, result.size());
        verify(medicalReportRepository, times(1)).findAll();
    }

    @Test
    void testGetReportById_success() {
        // Given
        Long reportId = 1L;
        MedicalReports report = new MedicalReports();
        report.setTitle("Report Title");

        when(medicalReportRepository.findById(reportId)).thenReturn(Optional.of(report));

        // When
        MedicalReports found = medicalReportService.getReportById(reportId);

        // Then
        assertNotNull(found);
        assertEquals("Report Title", found.getTitle());
        verify(medicalReportRepository, times(1)).findById(reportId);
    }

    @Test
    void testGetReportById_notFound() {
        // Given
        Long reportId = 1L;
        when(medicalReportRepository.findById(reportId)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                medicalReportService.getReportById(reportId)
        );
        assertTrue(exception.getMessage().contains("Report not found"));
        verify(medicalReportRepository, times(1)).findById(reportId);
    }

    @Test
    void testDownloadReport_success() throws IOException {
        // Given
        Long reportId = 1L;
        // Create a temporary file to simulate an existing report file
        File tempFile = File.createTempFile("tempReport", ".txt", new File(uploadDir));
        Files.write(tempFile.toPath(), "sample content".getBytes());

        MedicalReports report = new MedicalReports();
        report.setFileUrl(tempFile.getPath());
        when(medicalReportRepository.findById(reportId)).thenReturn(Optional.of(report));

        // When
        byte[] content = medicalReportService.downloadReport(reportId);

        // Then
        assertNotNull(content);
        assertEquals("sample content", new String(content));
        verify(medicalReportRepository, times(1)).findById(reportId);

        // Clean up temporary file
        tempFile.delete();
    }

    @Test
    void testGetReportsByType() {
        // Given
        // Replace 'LAB' with a valid constant from your ReportType enum
        ReportType reportType = ReportType.LAB;
        MedicalReports report1 = new MedicalReports();
        report1.setTitle("Report1");
        MedicalReports report2 = new MedicalReports();
        report2.setTitle("Report2");
        List<MedicalReports> reports = Arrays.asList(report1, report2);

        when(medicalReportRepository.findByReportType(reportType)).thenReturn(reports);

        // When
        List<MedicalReports> result = medicalReportService.getReportsByType(reportType);

        // Then
        assertEquals(2, result.size());
        verify(medicalReportRepository, times(1)).findByReportType(reportType);
    }

    @Test
    void testGetReportsByPatientAndType() {
        // Given
        Long patientId = 1L;
        // Replace 'LAB' with a valid constant from your ReportType enum
        ReportType reportType = ReportType.LAB;
        MedicalReports report1 = new MedicalReports();
        report1.setTitle("Report1");
        List<MedicalReports> reports = Arrays.asList(report1);

        when(medicalReportRepository.findByPatientMediIdAndReportType(patientId, reportType))
                .thenReturn(reports);

        // When
        List<MedicalReports> result = medicalReportService.getReportsByPatientAndType(patientId, reportType);

        // Then
        assertEquals(1, result.size());
        verify(medicalReportRepository, times(1))
                .findByPatientMediIdAndReportType(patientId, reportType);
    }
}
