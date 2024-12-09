package com.tokorokoshi.tokoro.modules.users.dto;

public record CreateUpdateUserDto(
        String username,
        String email,
        String password,
        UserPreferencesDto userPreferencesDto,
        UserFavoritesDto userFavoritesDto
) {
}
