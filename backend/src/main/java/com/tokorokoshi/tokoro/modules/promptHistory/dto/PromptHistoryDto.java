package com.tokorokoshi.tokoro.modules.promptHistory.dto;

import java.time.Instant;

public record PromptHistoryDto(
    String id,
    String prompt,
    String userId,
    Instant createdAt
) {
}
