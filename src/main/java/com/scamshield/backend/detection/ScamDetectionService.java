
package com.scamshield.backend.detection;

import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class ScamDetectionService {

    public DetectionResult analyze(String content) {
        List<String> reasons = new ArrayList<>();

        if (content == null || content.isBlank()) {
            reasons.add("No content was provided.");
            return new DetectionResult(0, "LOW", "Unknown", reasons);
        }

        String text = content.toLowerCase(Locale.ROOT);

        int score = 0;
        String category = "General Suspicious Message";

        if (containsOtp(text)) {
            score += 25;
            category = "Bank Phishing";
            reasons.add("OTP-related words detected.");
        }

        if (containsUrgency(text)) {
            score += 20;
            reasons.add("Urgent or threatening language detected.");
        }

        if (containsFinancialTerms(text)) {
            score += 20;
            category = "Financial Scam";
            reasons.add("Banking or payment-related terms detected.");
        }

        if (containsLink(text)) {
            score += 15;
            category = "Suspicious Link";
            reasons.add("A web link was detected.");
        }

        if (containsPrizeTerms(text)) {
            score += 25;
            category = "Prize Scam";
            reasons.add("Prize or reward-related language detected.");
        }

        if (containsAccountThreat(text)) {
            score += 20;
            reasons.add("Account suspension or closure threat detected.");
        }

        if (containsVerificationRequest(text)) {
            score += 15;
            reasons.add("Account or identity verification request detected.");
        }

        if (containsDeadline(text)) {
            score += 10;
            reasons.add("A time limit is being used to pressure the recipient.");
        }

        score = Math.min(score, 100);

        String riskLevel = getRiskLevel(score);

        if (reasons.isEmpty()) {
            reasons.add("No configured suspicious patterns were detected.");
        }

        return new DetectionResult(score, riskLevel, category, reasons);
    }

    private boolean containsOtp(String text) {
        return text.contains("otp")
                || text.contains("one time password")
                || text.contains("one-time password");
    }

    private boolean containsUrgency(String text) {
        return text.contains("urgent")
                || text.contains("immediately")
                || text.contains("account will be blocked")
                || text.contains("act fast")
                || text.contains("final warning")
                || text.contains("failure to act")
                || text.contains("right away");
    }

    private boolean containsFinancialTerms(String text) {
        return text.contains("bank")
                || text.contains("payment")
                || text.contains("upi")
                || text.contains("transfer money")
                || text.contains("credit card")
                || text.contains("debit card")
                || text.contains("kyc")
                || text.contains("account balance");
    }

    private boolean containsLink(String text) {
        return text.contains("http://")
                || text.contains("https://")
                || text.contains("www.");
    }

    private boolean containsPrizeTerms(String text) {
        return text.contains("won a prize")
                || text.contains("claim your reward")
                || text.contains("lottery")
                || text.contains("you are a winner");
    }

    private boolean containsAccountThreat(String text) {
        return text.contains("account will be blocked")
                || text.contains("permanently blocked")
                || text.contains("account suspended")
                || text.contains("temporarily suspended")
                || text.contains("account suspension")
                || text.contains("account closure")
                || text.contains("permanent account closure")
                || text.contains("account will be closed")
                || text.contains("restore access");
    }

    private boolean containsVerificationRequest(String text) {
        return text.contains("verify your account")
                || text.contains("verify your kyc")
                || text.contains("complete your kyc")
                || text.contains("confirm your identity")
                || text.contains("update your kyc")
                || text.contains("verify your identity");
    }

    private boolean containsDeadline(String text) {
        return text.contains("within 30 minutes")
                || text.contains("within 1 hour")
                || text.contains("within one hour")
                || text.contains("within 24 hours")
                || text.contains("before midnight")
                || text.contains("today")
                || text.contains("minutes");
    }

    private String getRiskLevel(int score) {
        if (score >= 80) {
            return "CRITICAL";
        }
        if (score >= 60) {
            return "HIGH";
        }
        if (score >= 30) {
            return "MEDIUM";
        }
        return "LOW";
    }
}
