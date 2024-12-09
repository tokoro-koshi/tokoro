package com.tokorokoshi.tokoro.database;

import com.mongodb.lang.NonNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "promptHistory")
public record PromptHistory(
        @Id
        String id,
        @NonNull
        String prompt,
        String userId,
        @Indexed
        @CreatedDate
        Instant createdAt
) {
        public PromptHistory withId(String id) {
                return new PromptHistory(id, prompt, userId, createdAt);
        }
}
