
package com.scamshield.backend.service;

import com.scamshield.backend.model.ScamReport;
import com.scamshield.backend.repository.ScamReportRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ScamReportService {

    private final ScamReportRepository repository;

    public ScamReportService(ScamReportRepository repository) {
        this.repository = repository;
    }

    public List<ScamReport> getAllReports() {
        return repository.findAll();
    }

    public Optional<ScamReport> getReportById(int id) {
        return repository.findById(id);
    }

    public ScamReport createReport(ScamReport report) {
        if (report.getStatus() == null ||
                report.getStatus().isBlank()) {
            report.setStatus("PENDING");
        }

        if (report.getRiskLevel() == null ||
                report.getRiskLevel().isBlank()) {
            report.setRiskLevel("UNKNOWN");
        }

        return repository.save(report);
    }

    public boolean updateReport(int id, ScamReport report) {
        return repository.update(id, report);
    }

    public boolean deleteReport(int id) {
        return repository.delete(id);
    }
    
public boolean updateReportStatus(int id, String status) {
    if (status == null ||
            !(status.equals("PENDING") ||
              status.equals("REVIEWED") ||
              status.equals("RESOLVED"))) {
        return false;
    }

    return repository.updateStatus(id, status);
}

}

