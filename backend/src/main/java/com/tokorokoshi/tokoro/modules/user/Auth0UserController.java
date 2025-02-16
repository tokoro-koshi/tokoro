package com.tokorokoshi.tokoro.modules.user;

import com.auth0.json.mgmt.users.User;
import com.tokorokoshi.tokoro.modules.auth0.Auth0ManagementService;
import com.tokorokoshi.tokoro.modules.auth0.Auth0UserDataService;
import com.tokorokoshi.tokoro.modules.error.NotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerErrorException;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Tag(name = "Users", description = "API for managing users")
@RestController
@RequestMapping("/api/users")
public class Auth0UserController {
    private final Auth0UserDataService auth0UserDataService;
    private final Auth0ManagementService auth0ManagementService;

    @Autowired
    public Auth0UserController(Auth0UserDataService auth0UserDataService, Auth0ManagementService auth0ManagementService) {
        this.auth0UserDataService = auth0UserDataService;
        this.auth0ManagementService = auth0ManagementService;
    }

    /**
     * Retrieves the currently authenticated user's email.
     *
     * @return the email of the currently authenticated user.
     */
    @Operation(
            summary = "Get authenticated user email",
            description = "Returns the email of the currently authenticated user"
    )
    @GetMapping("/email")
    public ResponseEntity<String> getAuthenticatedUserEmail() {
        try {
            String email = auth0UserDataService.getAuthenticatedUserEmail();
            return ResponseEntity.ok(email);
        } catch (Exception ex) {
            throw new ServerErrorException(
                    "Failed to retrieve authenticated user email",
                    ex
            );
        }
    }

    /**
     * Retrieves the currently authenticated user's name.
     *
     * @return the name of the currently authenticated user.
     */
    @Operation(
            summary = "Get authenticated user name",
            description = "Returns the name of the currently authenticated user"
    )
    @GetMapping("/name")
    public ResponseEntity<String> getAuthenticatedUserName() {
        try {
            String name = auth0UserDataService.getAuthenticatedUserName();
            return ResponseEntity.ok(name);
        } catch (Exception ex) {
            throw new ServerErrorException(
                    "Failed to retrieve authenticated user name",
                    ex
            );
        }
    }

    /**
     * Retrieves the currently authenticated user's permissions.
     *
     * @return a list of permissions for the currently authenticated user.
     */
    @Operation(
            summary = "Get authenticated user permissions",
            description = "Returns the permissions of the currently authenticated user"
    )
    @GetMapping("/permissions")
    public ResponseEntity<List<String>> getAuthenticatedUserPermissions() {
        try {
            List<String> permissions = auth0UserDataService.getAuthenticatedUserPermissions();
            return ResponseEntity.ok(permissions);
        } catch (Exception ex) {
            throw new ServerErrorException(
                    "Failed to retrieve authenticated user permissions",
                    ex
            );
        }
    }

    /**
     * Retrieves the currently authenticated user's metadata.
     *
     * @return a map of metadata for the currently authenticated user.
     */
    @Operation(
            summary = "Get authenticated user metadata",
            description = "Returns the metadata of the currently authenticated user"
    )
    @GetMapping("/metadata")
    public ResponseEntity<Map<String, Object>> getAuthenticatedUserMetadata() {
        try {
            Map<String, Object> metadata = auth0UserDataService.getAuthenticatedUserMetadata();
            return ResponseEntity.ok(metadata);
        } catch (Exception ex) {
            throw new ServerErrorException(
                    "Failed to retrieve authenticated user metadata",
                    ex
            );
        }
    }

    /**
     * Retrieves the currently authenticated user's roles.
     *
     * @return a list of roles for the currently authenticated user.
     */
    @Operation(
            summary = "Get authenticated user roles",
            description = "Returns the roles of the currently authenticated user"
    )
    @GetMapping("/roles")
    public ResponseEntity<List<String>> getAuthenticatedUserRoles() {
        try {
            List<String> roles = auth0UserDataService.getAuthenticatedUserRoles();
            return ResponseEntity.ok(roles);
        } catch (Exception ex) {
            throw new ServerErrorException(
                    "Failed to retrieve authenticated user roles",
                    ex
            );
        }
    }

    /**
     * Retrieves the full user object from Auth0 for the currently authenticated user.
     *
     * @return the {@link User} object representing the currently authenticated user.
     */
    @Operation(
            summary = "Get authenticated user details",
            description = "Returns the details of the currently authenticated user"
    )
    @GetMapping("/details")
    public ResponseEntity<User> getAuthenticatedUserDetails() {
        try {
            User userDetails = auth0UserDataService.getAuthenticatedUserDetails();
            return ResponseEntity.ok(userDetails);
        } catch (Exception ex) {
            throw new ServerErrorException(
                    "Failed to retrieve authenticated user details",
                    ex
            );
        }
    }

    /**
     * Checks if the currently authenticated user has a specific permission.
     *
     * @param permission the permission to check.
     * @return a response indicating whether the user has the permission.
     */
    @Operation(
            summary = "Check if user has permission",
            description = "Checks if the currently authenticated user has a specific permission"
    )
    @GetMapping("/permissions/check")
    public ResponseEntity<Boolean> hasPermission(
            @Parameter(description = "The permission to check", required = true, example = "read:users")
            @RequestParam
            @NotNull
            @NotBlank
            String permission
    ) {
        try {
            boolean hasPermission = auth0UserDataService.hasPermission(permission);
            return ResponseEntity.ok(hasPermission);
        } catch (Exception ex) {
            throw new ServerErrorException(
                    "Failed to check if user has permission",
                    ex
            );
        }
    }

    /**
     * Checks if the currently authenticated user has any of the specified permissions.
     *
     * @param permissions the list of permissions to check.
     * @return a response indicating whether the user has any of the permissions.
     */
    @Operation(
            summary = "Check if user has any permission",
            description = "Checks if the currently authenticated user has any of the specified permissions"
    )
    @GetMapping("/permissions/check-any")
    public ResponseEntity<Boolean> hasAnyPermission(
            @Parameter(description = "The list of permissions to check", required = true, example = "[\"read:users\", \"write:users\"]")
            @RequestParam
            @NotNull
            @NotEmpty
            List<@NotEmpty @NotBlank String> permissions
    ) {
        try {
            boolean hasAnyPermission = auth0UserDataService.hasAnyPermission(permissions);
            return ResponseEntity.ok(hasAnyPermission);
        } catch (Exception ex) {
            throw new ServerErrorException(
                    "Failed to check if user has any of the specified permissions",
                    ex
            );
        }
    }

    /**
     * Checks if the currently authenticated user has a specific role.
     *
     * @param role the role to check.
     * @return a response indicating whether the user has the role.
     */
    @Operation(
            summary = "Check if user has role",
            description = "Checks if the currently authenticated user has a specific role"
    )
    @GetMapping("/roles/check")
    public ResponseEntity<Boolean> hasRole(
            @Parameter(description = "The role to check", required = true, example = "admin")
            @RequestParam
            @NotNull
            @NotBlank
            String role
    ) {
        try {
            boolean hasRole = auth0UserDataService.hasRole(role);
            return ResponseEntity.ok(hasRole);
        } catch (Exception ex) {
            throw new ServerErrorException(
                    "Failed to check if user has role",
                    ex
            );
        }
    }

    /**
     * Checks if the currently authenticated user has any of the specified roles.
     *
     * @param roles the list of roles to check.
     * @return a response indicating whether the user has any of the roles.
     */
    @Operation(
            summary = "Check if user has any role",
            description = "Checks if the currently authenticated user has any of the specified roles"
    )
    @GetMapping("/roles/check-any")
    public ResponseEntity<Boolean> hasAnyRole(
            @Parameter(description = "The list of roles to check", required = true, example = "[\"admin\", \"user\"]")
            @RequestParam
            @NotNull
            @NotEmpty
            List<@NotEmpty @NotBlank String> roles
    ) {
        try {
            boolean hasAnyRole = auth0UserDataService.hasAnyRole(roles);
            return ResponseEntity.ok(hasAnyRole);
        } catch (Exception ex) {
            throw new ServerErrorException(
                    "Failed to check if user has any of the specified roles",
                    ex
            );
        }
    }

    /**
     * Checks if the user is blocked.
     *
     * @return a response indicating whether the user is blocked.
     */
    @Operation(
            summary = "Check if user is blocked",
            description = "Checks if the user is blocked"
    )
    @GetMapping("/{userId}/blocked")
    public ResponseEntity<Boolean> isUserBlocked(
            @Parameter(description = "The user ID to check", required = true, example = "auth0|60f1b3b3b3b3b3b3b3b3b3b")
            @PathVariable
            @NotNull
            @NotBlank
            String userId
    ) {
        try {
            boolean isBlocked = auth0ManagementService.isUserBlocked(userId);
            return ResponseEntity.ok(isBlocked);
        } catch (Exception ex) {
            throw new ServerErrorException(
                    "Failed to check if user is blocked",
                    ex
            );
        }
    }

    /**
     * Blocks the user.
     *
     * @return a response indicating success or failure.
     */
    @Operation(
            summary = "Block user",
            description = "Blocks the user and returns the string with operation status"
    )
    @PostMapping("/{userId}/block")
    public ResponseEntity<String> blockUser(
            @Parameter(description = "The user ID to block", required = true, example = "auth0|60f1b3b3b3b3b3b3b3b3b3b")
            @PathVariable
            @NotNull
            @NotBlank
            String userId
    ) {
        try {
            auth0ManagementService.blockUser(userId);
            return ResponseEntity.ok("User blocked successfully");
        } catch (Exception ex) {
            throw new ServerErrorException(
                    "Failed to block user",
                    ex
            );
        }
    }

    /**
     * Unblocks the user.
     *
     * @return a response indicating success or failure.
     */
    @Operation(
            summary = "Unblock user",
            description = "Unblocks the user and returns the string with operation status"
    )
    @PostMapping("/{userId}/unblock")
    public ResponseEntity<String> unblockUser(
            @Parameter(description = "The user ID to unblock", required = true, example = "auth0|60f1b3b3b3b3b3b3b3b3b3b")
            @PathVariable
            @NotNull
            @NotBlank
            String userId
    ) {
        try {
            auth0ManagementService.unblockUser(userId);
            return ResponseEntity.ok("User unblocked successfully");
        } catch (Exception ex) {
            throw new ServerErrorException(
                    "Failed to unblock user",
                    ex
            );
        }
    }


    /**
     * Updates the metadata for the currently authenticated user.
     *
     * @param metadata a map containing the metadata to be updated.
     * @return a response indicating success or failure.
     */
    @Operation(
            summary = "Update authenticated user metadata",
            description = "Updates the metadata for the currently authenticated user"
    )
    @PutMapping("/metadata")
    public ResponseEntity<String> updateAuthenticatedUserMetadata(
            @Parameter(description = "The metadata to update", required = true)
            @RequestBody
            @NotNull
            Map<String, Object> metadata
    ) {
        try {
            auth0UserDataService.updateAuthenticatedUserMetadata(metadata);
            return ResponseEntity.ok("User metadata updated successfully");
        } catch (Exception ex) {
            throw new ServerErrorException(
                    "Failed to update user metadata",
                    ex
            );
        }
    }

    /**
     * Updates the avatar URL for the currently authenticated user.
     *
     * @param avatarUrl the new avatar URL for the user.
     * @return a response indicating success or failure.
     */
    @Operation(
            summary = "Update authenticated user avatar",
            description = "Updates the avatar URL for the currently authenticated user"
    )
    @PutMapping("/avatar")
    public ResponseEntity<String> updateAuthenticatedUserAvatar(
            @Parameter(
                    description = "The avatar URL to update",
                    required = true,
                    example = "https://example.com/avatar.jpg"
            )
            @RequestParam
            @NotNull
            @NotBlank
            String avatarUrl
    ) {
        try {
            auth0UserDataService.updateAuthenticatedUserAvatar(avatarUrl);
            return ResponseEntity.ok("User avatar updated successfully");
        } catch (Exception ex) {
            throw new ServerErrorException(
                    "Failed to update user avatar",
                    ex
            );
        }
    }

    /**
     * Fetches the avatar URL for the currently authenticated user.
     *
     * @return a response containing the avatar URL or an error message.
     */
    @Operation(
            summary = "Get authenticated user avatar",
            description = "Returns the avatar URL of the currently authenticated user"
    )
    @GetMapping("/avatar")
    public ResponseEntity<String> getAuthenticatedUserAvatar() {
        try {
            String avatarUrl = auth0UserDataService.getAuthenticatedUserAvatar();
            return ResponseEntity.ok(Objects.requireNonNullElse(avatarUrl, "No avatar URL set for the user"));
        } catch (Exception ex) {
            throw new ServerErrorException(
                    "Failed to fetch user avatar",
                    ex
            );
        }
    }

    /**
     * Updates the first and last name for the currently authenticated user.
     *
     * @param firstName the new first name for the user.
     * @param lastName  the new last name for the user.
     * @return a response indicating success or failure.
     */
    @Operation(
            summary = "Update authenticated user name",
            description = "Updates the first and last name for the currently authenticated user"
    )
    @PutMapping("/name")
    public ResponseEntity<String> updateAuthenticatedUserName(
            @Parameter(
                    description = "The first name to update",
                    required = true,
                    example = "John"
            )
            @RequestParam
            @NotNull
            @NotBlank
            String firstName,
            @Parameter(
                    description = "The last name to update",
                    required = true,
                    example = "Doe"
            )
            @RequestParam
            @NotNull
            @NotBlank
            String lastName
    ) {
        try {
            auth0UserDataService.updateAuthenticatedUserName(firstName, lastName);
            return ResponseEntity.ok("User name updated successfully");
        } catch (Exception ex) {
            throw new ServerErrorException(
                    "Failed to update user name",
                    ex
            );
        }
    }

    /**
     * Retrieves the nickname of the currently authenticated user.
     *
     * @return the nickname of the currently authenticated user.
     */
    @Operation(
            summary = "Get authenticated user nickname",
            description = "Returns the nickname of the currently authenticated user"
    )
    @GetMapping("/nickname")
    public ResponseEntity<String> getAuthenticatedUserNickname() {
        try {
            String nickname = auth0UserDataService.getAuthenticatedUserNickname();
            return ResponseEntity.ok(nickname);
        } catch (Exception ex) {
            throw new ServerErrorException(
                    "Failed to retrieve authenticated user nickname",
                    ex
            );
        }
    }

    /**
     * Updates the nickname of the currently authenticated user.
     *
     * @param nickname the new nickname for the user.
     * @return a response indicating success or failure.
     */
    @Operation(
            summary = "Update authenticated user nickname",
            description = "Updates the nickname for the currently authenticated user"
    )
    @PutMapping("/nickname")
    public ResponseEntity<String> updateAuthenticatedUserNickname(
            @Parameter(
                    description = "The nickname to update",
                    required = true,
                    example = "johndoe"
            )
            @RequestParam
            @NotNull
            @NotBlank
            String nickname
    ) {
        try {
            auth0UserDataService.updateAuthenticatedUserNickname(nickname);
            return ResponseEntity.ok("User nickname updated successfully");
        } catch (Exception ex) {
            throw new ServerErrorException(
                    "Failed to update user nickname",
                    ex
            );
        }
    }

    /**
     * Fetches a user by their email address.
     *
     * @param email the email address of the user to fetch.
     * @return the {@link User} object representing the user, or a not found response.
     */
    @Operation(
            summary = "Get user by email",
            description = "Returns the user with the given email"
    )
    @GetMapping("/by-email")
    public ResponseEntity<User> getUserByEmail(
            @Parameter(
                    description = "The email of the user to fetch",
                    required = true,
                    example = "johndoe@example.com"
            )
            @RequestParam
            @NotNull
            @NotBlank
            String email
    ) {
        User user = auth0UserDataService.getUserByEmail(email);
        if (user == null) {
            throw new NotFoundException("User not found");
        }
        return ResponseEntity.ok(user);
    }

    /**
     * Deletes a user by their user ID.
     *
     * @param userId the user ID of the user to delete.
     * @return a response indicating success or failure.
     */
    @Operation(
            summary = "Delete user",
            description = "Deletes the user with the given user ID"
    )
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(
            @Parameter(
                    description = "The user ID of the user to delete",
                    required = true,
                    example = "auth0|60f1b3b3b3b3b3b3b3b3b3b"
            )
            @PathVariable
            @NotNull
            @NotBlank
            String userId
    ) {
        try {
            auth0ManagementService.deleteUser(userId);
            return ResponseEntity.ok("User deleted successfully");
        } catch (Exception ex) {
            throw new ServerErrorException(
                    "Failed to delete user with user ID: " + userId,
                    ex
            );
        }
    }

    /**
     * Assigns roles to a user by their user ID.
     *
     * @param userId the user ID of the user to assign roles to.
     * @param roles  the list of role IDs to assign.
     * @return a response indicating success or failure.
     */
    @Operation(
            summary = "Assign roles to user",
            description = "Assigns roles to the user with the given user ID"
    )
    @PostMapping("/{userId}/roles")
    public ResponseEntity<String> assignRolesToUser(
            @Parameter(
                    description = "The user ID of the user to assign roles to",
                    required = true,
                    example = "auth0|60f1b3b3b3b3b3b3b3b3b3b"
            )
            @PathVariable
            @NotNull
            @NotBlank
            String userId,
            @Parameter(
                    description = "The list of roles to assign",
                    required = true,
                    example = "[\"admin\", \"user\"]"
            )
            @RequestBody
            @NotNull
            @NotEmpty
            List<@NotEmpty @NotBlank String> roles
    ) {
        try {
            auth0ManagementService.assignRolesToUser(userId, roles);
            return ResponseEntity.ok("Roles assigned successfully");
        } catch (Exception ex) {
            throw new ServerErrorException(
                    "Failed to assign roles to user with user ID: " + userId,
                    ex
            );
        }
    }

    /**
     * Removes roles from a user by their user ID.
     *
     * @param userId the user ID of the user to remove roles from.
     * @param roles  the list of role IDs to remove.
     * @return a response indicating success or failure.
     */
    @Operation(
            summary = "Remove roles from user",
            description = "Removes roles from the user with the given user ID"
    )
    @DeleteMapping("/{userId}/roles")
    public ResponseEntity<String> removeRolesFromUser(
            @Parameter(
                    description = "The user ID of the user to remove roles from",
                    required = true,
                    example = "auth0|60f1b3b3b3b3b3b3b3b3b3b"
            )
            @PathVariable
            @NotNull
            @NotBlank
            String userId,
            @Parameter(
                    description = "The list of roles to remove",
                    required = true,
                    example = "[\"admin\", \"user\"]"
            )
            @RequestBody
            @NotNull
            @NotEmpty
            List<@NotEmpty @NotBlank String> roles
    ) {
        try {
            auth0ManagementService.removeRolesFromUser(userId, roles);
            return ResponseEntity.ok("Roles removed successfully");
        } catch (Exception ex) {
            throw new ServerErrorException(
                    "Failed to remove roles from user with user ID: " + userId,
                    ex
            );
        }
    }
}
