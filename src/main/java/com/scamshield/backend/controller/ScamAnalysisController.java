
package com.scamshield.backend.controller;

import com.scamshield.backend.detection.DetectionResult;
import com.scamshield.backend.detection.ScamAnalysisRequest;
import com.scamshield.backend.detection.ScamDetectionService;
import com.scamshield.backend.model.ScamReport;
import com.scamshield.backend.service.ScamReportService;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/analyze")
@CrossOrigin(origins = "http://localhost:5173")
public class ScamAnalysisController {

    private final ScamDetectionService detectionService;
    private final ScamReportService reportService;

    public ScamAnalysisController(
            ScamDetectionService detectionService,
            ScamReportService reportService) {
        this.detectionService = detectionService;
        this.reportService = reportService;
    }

    @PostMapping
    public ResponseEntity<DetectionResult> analyze(
            @Valid @RequestBody ScamAnalysisRequest request) {

        DetectionResult result =
                detectionService.analyze(request.getContent());

        return ResponseEntity.ok(result);
    }

    @PostMapping("/report")
    public ResponseEntity<Map<String, Object>> analyzeAndSaveReport(
            @Valid @RequestBody ScamAnalysisRequest request) {

        DetectionResult result =
                detectionService.analyze(request.getContent());

        ScamReport report = new ScamReport();
        report.setReporterName("Anonymous");
        report.setType("MESSAGE");
        report.setContent(request.getContent());
        report.setCategory(result.getCategory());
        report.setRiskLevel(result.getRiskLevel());
        report.setStatus("PENDING");

        ScamReport savedReport = reportService.createReport(report);

        return ResponseEntity.ok(
                Map.of(
                        "analysis", result,
                        "report", savedReport
                )
        );
    }
}
