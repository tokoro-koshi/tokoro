package com.tokorokoshi.tokoro.helpers;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.Instant;
import java.time.format.DateTimeParseException;

public class InstantDeserializer extends JsonDeserializer<Instant> {
    @Override
    public Instant deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String dateString = p.getText();
        try {
            // Handle case where timestamp has more than 9 digits of nanosecond precision
            int dotIndex = dateString.indexOf('.');
            if (dotIndex > 0) {
                dateString = dateString.substring(0, dotIndex);
            }
            if (!dateString.endsWith("Z")) {
                dateString += "Z"; // Append 'Z' to indicate UTC time zone
            }
            return Instant.parse(dateString);
        } catch (DateTimeParseException e) {
            throw new JsonParseException(p, "Failed to parse Instant: " + e.getMessage());
        }
    }
}
