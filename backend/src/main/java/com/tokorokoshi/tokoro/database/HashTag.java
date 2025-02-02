package com.tokorokoshi.tokoro.database;

import com.mongodb.lang.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "hashTag")
public record HashTag(
    @Id
    String id,
    @NonNull
    String lang,
    @NonNull
    String name
) {
}
