package com.tokorokoshi.tokoro.database;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Represents a 'Contact us' section in the database.
 */
@Document
public record ContactUs(
        @NotNull
        List<String> socialLinks,
        String phoneNumber
) {
}
