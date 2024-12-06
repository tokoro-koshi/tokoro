package com.tokorokoshi.tokoro.modules.place.dto;

public class CoordinateDto {
    private double latitude;
    private double longitude;

    public CoordinateDto() {
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
