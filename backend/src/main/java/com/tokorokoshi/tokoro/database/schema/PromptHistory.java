package com.tokorokoshi.tokoro.database.schema;

import com.mongodb.lang.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "promptHistory")
public class PromptHistory {
    @Id
    private ObjectId id;

    @NonNull
    private String prompt;

    private ObjectId userId;

    @Indexed
    @CreatedDate
    private Instant createdAt;

    public PromptHistory() {
        this.prompt = "";
    }

    @NonNull
    public ObjectId getId() {
        return id;
    }

    public void setId(@NonNull ObjectId id) {
        this.id = id;
    }

    @NonNull
    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(@NonNull String prompt) {
        this.prompt = prompt;
    }

    public ObjectId getUserId() {
        return userId;
    }

    public void setUserId(ObjectId userId) {
        this.userId = userId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
