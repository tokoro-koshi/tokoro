package com.tokorokoshi.tokoro.modules.userRatings.dto;

public record UserRatingDto(
        String id,
        String userId,
        String placeId,
        int value
) {
}
