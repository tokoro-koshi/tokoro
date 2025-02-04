package com.tokorokoshi.tokoro.modules.file;

import java.time.Instant;

/**
 * A file entry in the storage service.
 */
public record FileEntry(
        // The name of the file
        String name,
        // The type of the file
        String type,
        // The size of the file in bytes
        Long size,
        // The timestamp when the file was created
        Instant createdAt
) {}
