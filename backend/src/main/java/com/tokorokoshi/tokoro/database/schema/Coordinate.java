package com.tokorokoshi.tokoro.database.schema;

import com.mongodb.lang.NonNull;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Coordinate {
    @NonNull
    private double latitude;

    @NonNull
    private double longitude;

    public Coordinate() {
        this.latitude = 0;
        this.longitude = 0;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
