package com.tokorokoshi.tokoro.modules.promptHistory.dto;

import java.time.Instant;

public class CreateUpdatePromptHistoryDto {
    private String prompt;
    private String userId;
    private Instant createdAt;

    public CreateUpdatePromptHistoryDto() {
        this.prompt = "";
        this.userId = "";
        this.createdAt = Instant.now();
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

}
