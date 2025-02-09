package com.tokorokoshi.tokoro.database;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
public record ContactUs(
        List<String> socialLinks,
        String phoneNumber
) {
}
