package com.tokorokoshi.tokoro.modules.users.dto;

public record UserDto(
        String id,
        String username,
        String email,
        String password,
        UserPreferencesDto userPreferencesDto,
        UserFavoritesDto userFavoritesDto
) {
}
