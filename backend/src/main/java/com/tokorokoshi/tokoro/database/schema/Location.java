package com.tokorokoshi.tokoro.database.schema;

import com.mongodb.lang.NonNull;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Location {

    @NonNull
    private String address;

    @NonNull
    private String city;

    @NonNull
    private String country;

    @NonNull
    @DBRef
    private Coordinate coordinate;

    public Location() {
        this.address = "";
        this.city = "";
        this.country = "";
        this.coordinate = new Coordinate();
    }

    @NonNull
    public Coordinate getCoordinate() {
        return coordinate;
    }

    @NonNull
    public String getAddress() {
        return address;
    }

    @NonNull
    public String getCity() {
        return city;
    }

    @NonNull
    public String getCountry() {
        return country;
    }

    public void setAddress(@NonNull String address) {
        this.address = address;
    }

    public void setCoordinate(@NonNull Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public void setCity(@NonNull String city) {
        this.city = city;
    }

    public void setCountry(@NonNull String country) {
        this.country = country;
    }
}
