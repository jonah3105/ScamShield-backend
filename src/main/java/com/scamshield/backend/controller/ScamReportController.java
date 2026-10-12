
package com.scamshield.backend.controller;

import com.scamshield.backend.model.ScamReport;
import com.scamshield.backend.service.ScamReportService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "http://localhost:5173")
public class ScamReportController {
    private static final String REPORT_NOT_FOUND = "REPORT_NOT_FOUND";

    private final ScamReportService service;

    public ScamReportController(ScamReportService service) {
        this.service = service;
    }

    @GetMapping
    public List<ScamReport> getAllReports() {
        return service.getAllReports();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScamReport> getReportById(
            @PathVariable int id) {

        return service.getReportById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ScamReport> createReport(
            @RequestBody ScamReport report) {

        ScamReport savedReport = service.createReport(report);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedReport);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateReport(
            @PathVariable int id,
            @RequestBody ScamReport report) {

        boolean updated = service.updateReport(id, report);

        if (updated) {
            return ResponseEntity.ok("Report updated successfully");
        }

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(REPORT_NOT_FOUND);
    }
    
@PatchMapping("/{id}/status")
public ResponseEntity<String> updateReportStatus(
        @PathVariable int id,
        @RequestBody java.util.Map<String, String> request) {

    String status = request.get("status");

    if (status == null ||
            !(status.equals("PENDING") ||
              status.equals("REVIEWED") ||
              status.equals("RESOLVED"))) {
        return ResponseEntity.badRequest()
                .body("Invalid report status");
    }

    boolean updated = service.updateReportStatus(id, status);

    if (updated) {
        return ResponseEntity.ok("Report status updated successfully");
    }

    return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(REPORT_NOT_FOUND);
}


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteReport(
            @PathVariable int id) {

        boolean deleted = service.deleteReport(id);

        if (deleted) {
            return ResponseEntity.ok("Report deleted successfully");
        }

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(REPORT_NOT_FOUND);
    }
}
