package com.tokorokoshi.tokoro.database;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document
public record Comment(
        @Id
        String userId,
        @NotNull
        String value,
        @Indexed
        @CreatedDate
        Instant createdAt,
        @Indexed
        @LastModifiedDate
        Instant updatedAt
) {
}
