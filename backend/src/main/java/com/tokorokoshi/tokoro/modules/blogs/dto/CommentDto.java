package com.tokorokoshi.tokoro.modules.blogs.dto;

import java.time.Instant;

public class CommentDto {
    private String userId;
    private String value;
    private Instant createdAt;
    private Instant updatedAt;

    public CommentDto(){
        this.userId = "";
        this.value = "";
        this.createdAt = null;
        this.updatedAt = null;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
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
