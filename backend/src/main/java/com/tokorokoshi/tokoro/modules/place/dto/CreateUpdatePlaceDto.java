package com.tokorokoshi.tokoro.modules.place.dto;

import java.util.List;

public class CreateUpdatePlaceDto {

    private String name;
    private String description;
    private LocationDto locationDto;
    private String categoryId;
    private List<HashTagDto> hashTagsDto;
    private double rating;

    public CreateUpdatePlaceDto() {
        this.name = "";
        this.description = "";
        this.locationDto = new LocationDto();
        this.categoryId = "";
        this.hashTagsDto = List.of();
        this.rating = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocationDto getLocation() {
        return locationDto;
    }

    public void setLocation(LocationDto location) {
        this.locationDto = location;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public List<HashTagDto> getHashTags() {
        return hashTagsDto;
    }

    public void setHashTags(List<HashTagDto> hashTags) {
        this.hashTagsDto = hashTags;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }


}

