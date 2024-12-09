package com.tokorokoshi.tokoro.modules.promptHistory.dto;

import java.time.Instant;

public record CreateUpdatePromptHistoryDto(
        String prompt,
        String userId,
        Instant createdAt
) {
}
