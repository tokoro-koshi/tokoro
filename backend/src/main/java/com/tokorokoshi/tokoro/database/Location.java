package com.tokorokoshi.tokoro.database;

import com.mongodb.lang.NonNull;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Represents a location in the database.
 */
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
        @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
        GeoJsonPoint coordinate
) {
}
