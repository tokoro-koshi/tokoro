package com.tokorokoshi.tokoro.database;

import com.mongodb.lang.NonNull;

/**
 * Represents a hashtag in the database.
 */
public record HashTag(
    @NonNull
    String lang,
    @NonNull
    String name
) {
}
