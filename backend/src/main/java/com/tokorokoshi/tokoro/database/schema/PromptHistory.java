package com.tokorokoshi.tokoro.database.schema;

import com.mongodb.lang.NonNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "promptHistory")
public class PromptHistory {
    @Id
    private String id;

    @NonNull
    private String prompt;

    private String userId;

    @Indexed
    @CreatedDate
    private Instant createdAt;

    public PromptHistory() {
        this.prompt = "";
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    @NonNull
    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(@NonNull String prompt) {
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
