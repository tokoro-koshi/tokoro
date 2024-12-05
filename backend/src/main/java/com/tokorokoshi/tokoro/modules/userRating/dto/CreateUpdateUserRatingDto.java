package com.tokorokoshi.tokoro.modules.userRating.dto;

public class CreateUpdateUserRatingDto {
    private String userId;
    private String placeId;
    private int value;

    public CreateUpdateUserRatingDto() {
        this.value = 0;
        this.placeId = "";
        this.userId = "";
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
