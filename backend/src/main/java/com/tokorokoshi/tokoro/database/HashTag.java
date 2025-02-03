package com.tokorokoshi.tokoro.database;

import com.mongodb.lang.NonNull;

public record HashTag(
    @NonNull
    String lang,
    @NonNull
    String name
) {
}
