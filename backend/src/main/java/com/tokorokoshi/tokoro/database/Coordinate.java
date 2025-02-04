package com.tokorokoshi.tokoro.database;

import com.mongodb.lang.NonNull;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Represents a coordinate in the database.
 */
@Document
public record Coordinate(
        @NonNull
        double latitude,
        @NonNull
        double longitude
) {
}
