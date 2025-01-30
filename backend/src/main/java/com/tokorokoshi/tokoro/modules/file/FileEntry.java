package com.tokorokoshi.tokoro.modules.file;

import java.time.Instant;

public record FileEntry(
        String name,
        String type,
        Long size,
        Instant createdAt
) {}
