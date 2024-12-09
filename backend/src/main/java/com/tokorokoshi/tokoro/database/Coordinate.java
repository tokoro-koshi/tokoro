package com.tokorokoshi.tokoro.database;

import com.mongodb.lang.NonNull;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public record Coordinate(
        @NonNull
        double latitude,
        @NonNull
        double longitude
) {}
