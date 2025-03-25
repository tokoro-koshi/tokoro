package com.tokorokoshi.tokoro.modules.users.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * A DTO for a user.
 */
@Schema(
        name = "UserDto",
        description = "A Data Transfer Object (DTO) for a user"
)
public record UserDto(
        @Schema(
                name = "username",
                description = "The username of the user"
        )
        String username,

        @Schema(
                name = "email",
                description = "The email address of the user"
        )
        String email,

        @Schema(
                name = "phoneNumber",
                description = "The phone number of the user"
        )
        String phoneNumber,

        @Schema(
                name = "picture",
                description = "The URL of the user's profile picture"
        )
        String picture,

        @Schema(
                name = "name",
                description = "The full name of the user"
        )
        String name,

        @Schema(
                name = "nickname",
                description = "The nickname of the user"
        )
        String nickname,

        @Schema(
                name = "givenName",
                description = "The given name of the user"
        )
        String givenName,

        @Schema(
                name = "familyName",
                description = "The family name of the user"
        )
        String familyName,

        @Schema(
                name = "createdAt",
                description = "The date and time the user was created"
        )
        Date createdAt,

        @Schema(
                name = "updatedAt",
                description = "The date and time the user was last updated"
        )
        Date updatedAt,

        @Schema(
                name = "appMetadata",
                description = "The metadata associated with the user's application"
        )
        Map<String, Object> appMetadata,

        @Schema(
                name = "userMetadata",
                description = "The metadata associated with the user"
        )
        Map<String, Object> userMetadata,

        @Schema(
                name = "blocked",
                description = "Whether the user is blocked"
        )
        Boolean blocked,

        @Schema(
                name = "values",
                description = "Additional values associated with the user"
        )
        Map<String, Object> values,

        @Schema(
                name = "roles",
                description = "The roles assigned to the user"
        )
        @Nullable List<String> roles,

        @Schema(
                name = "permissions",
                description = "The permissions assigned to the user"
        )
        @Nullable List<String> permissions
) {
    public UserDto {
        if (roles == null) {
            roles = List.of();
        }
        if (permissions == null) {
            permissions = List.of();
        }
    }

    /**
     * Adds roles to the user.
     *
     * @param roles the roles to add to the user.
     * @return a new {@link UserDto} object with the added roles.
     */
    public UserDto withRoles(List<String> roles) {
        return new UserDto(
                username,
                email,
                phoneNumber,
                picture,
                name,
                nickname,
                givenName,
                familyName,
                createdAt,
                updatedAt,
                appMetadata,
                userMetadata,
                blocked,
                values,
                roles,
                permissions
        );
    }

    /**
     * Adds permissions to the user.
     *
     * @param permissions the permissions to add to the user.
     * @return a new {@link UserDto} object with the added permissions.
     */
    public UserDto withPermissions(List<String> permissions) {
        return new UserDto(
                username,
                email,
                phoneNumber,
                picture,
                name,
                nickname,
                givenName,
                familyName,
                createdAt,
                updatedAt,
                appMetadata,
                userMetadata,
                blocked,
                values,
                roles,
                permissions
        );
    }
}
