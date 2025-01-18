package com.tokorokoshi.tokoro.database;

import com.mongodb.lang.NonNull;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
public record UserPreferences(
    @NonNull
    String language,
    @NonNull
    List<String> tags,
    @NonNull
    List<String> categories
) {
}
