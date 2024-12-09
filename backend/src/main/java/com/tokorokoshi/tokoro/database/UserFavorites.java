package com.tokorokoshi.tokoro.database;

import com.mongodb.lang.NonNull;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
public record UserFavorites(
        @NonNull
        List<String> places,
        @NonNull
        List<String> prompts
) {}
