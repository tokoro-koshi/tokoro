package com.tokorokoshi.tokoro.database;

import com.mongodb.lang.NonNull;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


public class Location {
    @NonNull
    private String address;

    @NonNull
    private String city;

    @NonNull
    private String country;

    @NonNull
    @Field
    private Coordinate coordinate;

    public Location() {
        this.address = "";
        this.city = "";
        this.country = "";
        this.coordinate = new Coordinate();
    }

    @NonNull
    public String getAddress() {
        return address;
    }

    public void setAddress(@NonNull String address) {
        this.address = address;
    }

    @NonNull
    public String getCity() {
        return city;
    }

    public void setCity(@NonNull String city) {
        this.city = city;
    }

    @NonNull
    public String getCountry() {
        return country;
    }

    public void setCountry(@NonNull String country) {
        this.country = country;
    }

    @NonNull
    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(@NonNull Coordinate coordinate) {
        this.coordinate = coordinate;
    }
}
