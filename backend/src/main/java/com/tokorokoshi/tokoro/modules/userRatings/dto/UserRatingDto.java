package com.tokorokoshi.tokoro.modules.userRatings.dto;

public class UserRatingDto {
    private String id;
    private String userId;
    private String placeId;
    private int value;

    public UserRatingDto() {
        this.id = "";
        this.userId = "";
        this.placeId = "";
        this.value = 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
