package com.tokorokoshi.tokoro.modules.users.dto;

import java.util.List;

public record UserPreferencesDto(
        String language,
        List<String> tags,
        List<String> categories
) {
}
