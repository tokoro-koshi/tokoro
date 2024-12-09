package com.tokorokoshi.tokoro.database;

import com.mongodb.lang.NonNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Document(collection = "blog")
public record Blog(
        @Id
        String id,
        @NonNull
        String title,
        @NonNull
        String content,
        @NonNull
        List<String> authorId,
        @NonNull
        List<String> tags,
        List<Comment> comments,
        @Indexed
        @CreatedDate
        Instant createdAt,
        @Indexed
        @LastModifiedDate
        Instant updatedAt
) {
    public Blog withId(String id) {
        return new Blog(id, title, content, authorId, tags, comments, createdAt, updatedAt);
    }
}
