package com.tokorokoshi.tokoro.database;

import com.mongodb.lang.NonNull;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document
public record Location(
    @NonNull
    String address,
    @NonNull
    String city,
    @NonNull
    String country,
    @NonNull
    @Field
    Coordinate coordinate
) {
}
