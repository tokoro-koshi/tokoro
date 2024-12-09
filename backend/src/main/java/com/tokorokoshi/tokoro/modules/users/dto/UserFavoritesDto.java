package com.tokorokoshi.tokoro.modules.users.dto;

import java.util.List;

public record UserFavoritesDto(
        List<String> places,
        List<String> prompts
) {}
