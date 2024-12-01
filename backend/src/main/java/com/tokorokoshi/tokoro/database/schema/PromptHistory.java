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
    private Instant created_at;

    public PromptHistory(){
        this.prompt = "";

    }

    @NonNull
    public String getPrompt() {
        return prompt;
    }

    public Instant getCreated_at() {
        return created_at;
    }

    public ObjectId getId() {
        return id;
    }

    public ObjectId getUserId() {
        return userId;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public void setCreated_at(Instant created_at) {
        this.created_at = created_at;
    }

    public void setUserId(ObjectId userId) {
        this.userId = userId;
    }

    public void setPrompt(@NonNull String prompt) {
        this.prompt = prompt;
    }
}
