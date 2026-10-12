
package com.scamshield.backend.detection;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ScamAnalysisRequest {

    @NotBlank(message = "Message content cannot be empty.")
    @Size(max = 5000, message = "Message content cannot exceed 5000 characters.")
    private String content;

    public ScamAnalysisRequest() {
        // Default constructor for receiving JSON data.
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
