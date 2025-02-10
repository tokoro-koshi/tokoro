package com.tokorokoshi.tokoro.database;

import java.time.LocalDate;
import java.util.Map;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Represents a privacy section in the database.
 */
@Document("privacy")
public record Privacy(
        @Id
        String id,
        @NotNull
        String title,
        @NotNull
        LocalDate effectiveDate,
        @NotNull
        @LastModifiedDate
        LocalDate lastUpdated,
        String introduction,
        @NotNull
        Map<String, String> informationWeCollect,
        @NotNull
        Map<String, String> howWeUseCollectedInformation,
        String howWeShareUserInformation,
        String howWeProtectUserInformation,
        String yourRights,
        @Field
        ContactUs contactUs
) {
    /**
     * Creates a new privacy policy with the given ID.
     *
     * @param id The ID of the privacy policy
     * @return A new privacy policy with the given id
     */
    public Privacy withId(String id) {
        return new Privacy(
                id,
                title,
                effectiveDate,
                lastUpdated,
                introduction,
                informationWeCollect,
                howWeUseCollectedInformation,
                howWeShareUserInformation,
                howWeProtectUserInformation,
                yourRights,
                contactUs
        );
    }
}