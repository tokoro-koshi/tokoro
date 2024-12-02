package com.tokorokoshi.tokoro.database.schema;

import com.mongodb.lang.NonNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Document(collection = "blog")
public class Blog {
    @Id
    private String id;

    @NonNull
    private String title;

    @NonNull
    private String content;

    @NonNull
    @DBRef
    private List<String> authorId;

    @NonNull
    @DBRef
    private List<String> tags;

    @DBRef
    private List<Comment> comments;

    @Indexed
    @CreatedDate
    private Instant createdAt;

    @Indexed
    @LastModifiedDate
    private Instant updatedAt;

    public Blog() {
        this.title = "";
        this.content = "";
        this.authorId = List.of();
        this.tags = List.of();
        this.comments = List.of();

    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    @NonNull
    public String getContent() {
        return content;
    }

    public void setContent(@NonNull String content) {
        this.content = content;
    }

    @NonNull
    public List<String> getAuthorId() {
        return authorId;
    }

    public void setAuthorId(@NonNull List<String> authorId) {
        this.authorId = authorId;
    }

    @NonNull
    public List<String> getTags() {
        return tags;
    }

    public void setTags(@NonNull List<String> tags) {
        this.tags = tags;
    }

    @NonNull
    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(@NonNull List<Comment> comments) {
        this.comments = comments;
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
