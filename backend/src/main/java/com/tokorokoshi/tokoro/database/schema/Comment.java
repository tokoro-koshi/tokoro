package com.tokorokoshi.tokoro.database.schema;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document
public class Comment {
    @Id
    private ObjectId userId;

    @NotNull
    private String value;

    @Indexed
    @CreatedDate
    private Instant createdAt;

    @Indexed
    @LastModifiedDate
    private Instant updatedAt;

    public Comment() {
        this.value = "";
    }

    @NotNull
    public ObjectId getUserId() {
        return userId;
    }

    public void setUserId(@NotNull ObjectId userId) {
        this.userId = userId;
    }

    @NotNull
    public String getValue() {
        return value;
    }

    public void setValue(@NotNull String value) {
        this.value = value;
    }


    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
