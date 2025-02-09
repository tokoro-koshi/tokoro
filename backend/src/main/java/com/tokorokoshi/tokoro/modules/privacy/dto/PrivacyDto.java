package com.tokorokoshi.tokoro.modules.privacy.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.Map;

/**
 * A DTO for the Privacy information
 */
@Schema(
        name = "PrivacyDto",
        description = "A DTO for the Privacy information"
)
public record PrivacyDto(
        @Schema(
                name = "id",
                description = "The ID of the Privacy document"
        )
        String id,
        @Schema(
                name = "title",
                description = "The title of the Privacy section"
        )
        String title,
        @Schema(
                name = "effectiveDate",
                description = "The effective date of the Privacy policy"
        )
        LocalDate effectiveDate,
        @Schema(
                name = "lastUpdated",
                description = "The last updated date of the Privacy policy"
        )
        LocalDate lastUpdated,
        @Schema(
                name = "introduction",
                description = "The introduction text of the Privacy policy"
        )
        String introduction,
        @Schema(
                name = "informationWeCollect",
                description = "A map of types of information we collect and descriptions"
        )
        Map<String, String> informationWeCollect,
        @Schema(
                name = "howWeUseCollectedInformation",
                description = "A map of how we use collected information and descriptions"
        )
        Map<String, String> howWeUseCollectedInformation,
        @Schema(
                name = "howWeShareUserInformation",
                description = "How we share user information"
        )
        String howWeShareUserInformation,
        @Schema(
                name = "howWeProtectUserInformation",
                description = "How we protect user information"
        )
        String howWeProtectUserInformation,
        @Schema(
                name = "yourRights",
                description = "Your rights regarding your information"
        )
        String yourRights,
        @Schema(
                name = "contactUs",
                description = "Contact information for privacy inquiries"
        )
        ContactUsDto contactUs
) {
}