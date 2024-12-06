package com.tokorokoshi.tokoro.modules.place.dto;

public class LocationDto {
    private String address;
    private String city;
    private String country;
    private CoordinateDto coordinate;

    public LocationDto() {
        this.address = "";
        this.city = "";
        this.country = "";
        this.coordinate = new CoordinateDto();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public CoordinateDto getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(CoordinateDto coordinate) {
        this.coordinate = coordinate;
    }
}
