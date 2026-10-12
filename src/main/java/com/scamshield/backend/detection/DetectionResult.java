
package com.scamshield.backend.detection;

import java.util.List;

public class DetectionResult {

    private int score;
    private String riskLevel;
    private String category;
    private List<String> reasons;

    public DetectionResult() {
    }

    public DetectionResult(
            int score,
            String riskLevel,
            String category,
            List<String> reasons) {
        this.score = score;
        this.riskLevel = riskLevel;
        this.category = category;
        this.reasons = reasons;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<String> getReasons() {
        return reasons;
    }

    public void setReasons(List<String> reasons) {
        this.reasons = reasons;
    }
}
