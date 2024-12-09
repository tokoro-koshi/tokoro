package com.tokorokoshi.tokoro.modules.userRatings.dto;

public record CreateUpdateUserRatingDto(
        String userId,
        String placeId,
        int value
) {
}
