package com.tokorokoshi.tokoro.modules.user;

import com.auth0.json.mgmt.users.User;
import com.tokorokoshi.tokoro.modules.auth0.Auth0ManagementService;
import com.tokorokoshi.tokoro.modules.auth0.Auth0UserDataService;
import com.tokorokoshi.tokoro.modules.exceptions.auth0.Auth0ManagementException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/users")
public class Auth0UserController {

    private static final Logger log = LoggerFactory.getLogger(Auth0UserController.class);

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
    @GetMapping("/email")
    public ResponseEntity<String> getAuthenticatedUserEmail() {
        try {
            String email = auth0UserDataService.getAuthenticatedUserEmail();
            return ResponseEntity.ok(email);
        } catch (Exception e) {
            log.error("Error retrieving authenticated user email", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to retrieve authenticated user email: " + e.getMessage());
        }
    }

    /**
     * Retrieves the currently authenticated user's name.
     *
     * @return the name of the currently authenticated user.
     */
    @GetMapping("/name")
    public ResponseEntity<String> getAuthenticatedUserName() {
        try {
            String name = auth0UserDataService.getAuthenticatedUserName();
            return ResponseEntity.ok(name);
        } catch (Exception e) {
            log.error("Error retrieving authenticated user name", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to retrieve authenticated user name: " + e.getMessage());
        }
    }

    /**
     * Retrieves the currently authenticated user's permissions.
     *
     * @return a list of permissions for the currently authenticated user.
     */
    @GetMapping("/permissions")
    public ResponseEntity<List<String>> getAuthenticatedUserPermissions() {
        try {
            List<String> permissions = auth0UserDataService.getAuthenticatedUserPermissions();
            return ResponseEntity.ok(permissions);
        } catch (Exception e) {
            log.error("Error retrieving authenticated user permissions", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(List.of()); // Return an empty list
        }
    }

    /**
     * Retrieves the currently authenticated user's metadata.
     *
     * @return a map of metadata for the currently authenticated user.
     */
    @GetMapping("/metadata")
    public ResponseEntity<Map<String, Object>> getAuthenticatedUserMetadata() {
        try {
            Map<String, Object> metadata = auth0UserDataService.getAuthenticatedUserMetadata();
            return ResponseEntity.ok(metadata);
        } catch (Exception e) {
            log.error("Error retrieving authenticated user metadata", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of()); // Return an empty map
        }
    }

    /**
     * Retrieves the currently authenticated user's roles.
     *
     * @return a list of roles for the currently authenticated user.
     */
    @GetMapping("/roles")
    public ResponseEntity<List<String>> getAuthenticatedUserRoles() {
        try {
            List<String> roles = auth0UserDataService.getAuthenticatedUserRoles();
            return ResponseEntity.ok(roles);
        } catch (Exception e) {
            log.error("Error retrieving authenticated user roles", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(List.of()); // Return an empty list
        }
    }

    /**
     * Retrieves the full user object from Auth0 for the currently authenticated user.
     *
     * @return the {@link User} object representing the currently authenticated user.
     */
    @GetMapping("/details")
    public ResponseEntity<User> getAuthenticatedUserDetails() {
        try {
            User userDetails = auth0UserDataService.getAuthenticatedUserDetails();
            return ResponseEntity.ok(userDetails);
        } catch (Exception e) {
            log.error("Error retrieving authenticated user details", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null); // Return null for internal server error
        }
    }

    /**
     * Checks if the currently authenticated user has a specific permission.
     *
     * @param permission the permission to check.
     * @return a response indicating whether the user has the permission.
     */
    @GetMapping("/permissions/check")
    public ResponseEntity<Boolean> hasPermission(
            @RequestParam @NotNull @NotBlank String permission) {
        try {
            boolean hasPermission = auth0UserDataService.hasPermission(permission);
            return ResponseEntity.ok(hasPermission);
        } catch (Exception e) {
            log.error("Error checking if user has permission", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(false); // Return false for internal server error
        }
    }

    /**
     * Checks if the currently authenticated user has any of the specified permissions.
     *
     * @param permissions the list of permissions to check.
     * @return a response indicating whether the user has any of the permissions.
     */
    @GetMapping("/permissions/check-any")
    public ResponseEntity<Boolean> hasAnyPermission(
            @RequestParam @NotNull @NotEmpty List<@NotEmpty @NotBlank String> permissions) {
        try {
            boolean hasAnyPermission = auth0UserDataService.hasAnyPermission(permissions);
            return ResponseEntity.ok(hasAnyPermission);
        } catch (Exception e) {
            log.error("Error checking if user has any of the specified permissions", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(false); // Return false for internal server error
        }
    }

    /**
     * Checks if the currently authenticated user has a specific role.
     *
     * @param role the role to check.
     * @return a response indicating whether the user has the role.
     */
    @GetMapping("/roles/check")
    public ResponseEntity<Boolean> hasRole(
            @RequestParam @NotNull @NotBlank String role) {
        try {
            boolean hasRole = auth0UserDataService.hasRole(role);
            return ResponseEntity.ok(hasRole);
        } catch (Exception e) {
            log.error("Error checking if user has role", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(false); // Return false for internal server error
        }
    }

    /**
     * Checks if the currently authenticated user has any of the specified roles.
     *
     * @param roles the list of roles to check.
     * @return a response indicating whether the user has any of the roles.
     */
    @GetMapping("/roles/check-any")
    public ResponseEntity<Boolean> hasAnyRole(
            @RequestParam @NotNull @NotEmpty List<@NotEmpty @NotBlank String> roles) {
        try {
            boolean hasAnyRole = auth0UserDataService.hasAnyRole(roles);
            return ResponseEntity.ok(hasAnyRole);
        } catch (Exception e) {
            log.error("Error checking if user has any of the specified roles", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(false); // Return false for internal server error
        }
    }

    /**
     * Checks if the user is blocked.
     *
     * @return a response indicating whether the user is blocked.
     */
    @GetMapping("/{userId}/blocked")
    public ResponseEntity<Boolean> isUserBlocked(@PathVariable @NotNull @NotBlank String userId) {
        try {
            boolean isBlocked = auth0ManagementService.isUserBlocked(userId);
            return ResponseEntity.ok(isBlocked);
        } catch (Exception e) {
            log.error("Error checking if user is blocked", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    /**
     * Blocks the user.
     *
     * @return a response indicating success or failure.
     */
    @PostMapping("/{userId}/block")
    public ResponseEntity<String> blockUser(@PathVariable @NotNull @NotBlank String userId) {
        try {
            auth0ManagementService.blockUser(userId);
            return ResponseEntity.ok("User blocked successfully");
        } catch (Exception e) {
            log.error("Error blocking user", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to block user: " + e.getMessage());
        }
    }

    /**
     * Unblocks the user.
     *
     * @return a response indicating success or failure.
     */
    @PostMapping("/{userId}/unblock")
    public ResponseEntity<String> unblockUser(@PathVariable @NotNull @NotBlank String userId) {
        try {
            auth0ManagementService.unblockUser(userId);
            return ResponseEntity.ok("User unblocked successfully");
        } catch (Exception e) {
            log.error("Error unblocking user", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to unblock user: " + e.getMessage());
        }
    }


    /**
     * Updates the metadata for the currently authenticated user.
     *
     * @param metadata a map containing the metadata to be updated.
     * @return a response indicating success or failure.
     */
    @PutMapping("/metadata")
    public ResponseEntity<String> updateAuthenticatedUserMetadata(
            @RequestBody @NotNull Map<String, Object> metadata) {
        try {
            auth0UserDataService.updateAuthenticatedUserMetadata(metadata);
            return ResponseEntity.ok("User metadata updated successfully");
        } catch (Exception e) {
            log.error("Error updating user metadata", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update user metadata: " + e.getMessage());
        }
    }

    /**
     * Updates the avatar URL for the currently authenticated user.
     *
     * @param avatarUrl the new avatar URL for the user.
     * @return a response indicating success or failure.
     */
    @PutMapping("/avatar")
    public ResponseEntity<String> updateAuthenticatedUserAvatar(
            @RequestParam @NotNull @NotBlank String avatarUrl) {
        try {
            auth0UserDataService.updateAuthenticatedUserAvatar(avatarUrl);
            return ResponseEntity.ok("User avatar updated successfully");
        } catch (Exception e) {
            log.error("Error updating user avatar", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update user avatar: " + e.getMessage());
        }
    }

    /**
     * Fetches the avatar URL for the currently authenticated user.
     *
     * @return a response containing the avatar URL or an error message.
     */
    @GetMapping("/avatar")
    public ResponseEntity<String> getAuthenticatedUserAvatar() {
        try {
            String avatarUrl = auth0UserDataService.getAuthenticatedUserAvatar();
            return ResponseEntity.ok(Objects.requireNonNullElse(avatarUrl, "No avatar URL set for the user"));
        } catch (Exception e) {
            log.error("Error fetching user avatar", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch user avatar: " + e.getMessage());
        }
    }

    /**
     * Updates the first and last name for the currently authenticated user.
     *
     * @param firstName the new first name for the user.
     * @param lastName the new last name for the user.
     * @return a response indicating success or failure.
     */
    @PutMapping("/name")
    public ResponseEntity<String> updateAuthenticatedUserName(
            @RequestParam @NotNull @NotBlank String firstName,
            @RequestParam @NotNull @NotBlank String lastName) {
        try {
            auth0UserDataService.updateAuthenticatedUserName(firstName, lastName);
            return ResponseEntity.ok("User name updated successfully");
        } catch (Exception e) {
            log.error("Error updating user name", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update user name: " + e.getMessage());
        }
    }

    /**
     * Retrieves the nickname of the currently authenticated user.
     *
     * @return the nickname of the currently authenticated user.
     */
    @GetMapping("/nickname")
    public ResponseEntity<String> getAuthenticatedUserNickname() {
        try {
            String nickname = auth0UserDataService.getAuthenticatedUserNickname();
            return ResponseEntity.ok(nickname);
        } catch (Exception e) {
            log.error("Error retrieving authenticated user nickname", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to retrieve authenticated user nickname: " + e.getMessage());
        }
    }

    /**
     * Updates the nickname of the currently authenticated user.
     *
     * @param nickname the new nickname for the user.
     * @return a response indicating success or failure.
     */
    @PutMapping("/nickname")
    public ResponseEntity<String> updateAuthenticatedUserNickname(
            @RequestParam @NotNull @NotBlank String nickname) {
        try {
            auth0UserDataService.updateAuthenticatedUserNickname(nickname);
            return ResponseEntity.ok("User nickname updated successfully");
        } catch (Exception e) {
            log.error("Error updating user nickname", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update user nickname: " + e.getMessage());
        }
    }

    /**
     * Fetches a user by their email address.
     *
     * @param email the email address of the user to fetch.
     * @return the {@link User} object representing the user, or a not found response.
     */
    @GetMapping("/by-email")
    public ResponseEntity<User> getUserByEmail(
            @RequestParam @NotNull @NotBlank String email) {
        try {
            User user = auth0UserDataService.getUserByEmail(email);
            if (user != null) {
                return ResponseEntity.ok(user);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(null); // Return null for not found
            }
        } catch (Exception e) {
            log.error("Error fetching user by email", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null); // Return null for internal server error
        }
    }

    /**
     * Deletes a user by their user ID.
     *
     * @param userId the user ID of the user to delete.
     * @return a response indicating success or failure.
     * @throws Auth0ManagementException if there is an error deleting the user.
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(
            @PathVariable @NotNull @NotBlank String userId) {
        try {
            log.debug("Deleting user with user ID: {}", userId);
            auth0ManagementService.deleteUser(userId);
            log.info("Successfully deleted user with user ID: {}", userId);
            return ResponseEntity.ok("User deleted successfully");
        } catch (Auth0ManagementException e) {
            log.error("Error deleting user with user ID: {}", userId, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to delete user: " + e.getMessage());
        } catch (Exception e) {
            log.error("Error deleting user with user ID: {}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete user: " + e.getMessage());
        }
    }

    /**
     * Assigns roles to a user by their user ID.
     *
     * @param userId the user ID of the user to assign roles to.
     * @param roles the list of role IDs to assign.
     * @return a response indicating success or failure.
     * @throws Auth0ManagementException if there is an error assigning roles.
     */
    @PostMapping("/{userId}/roles")
    public ResponseEntity<String> assignRolesToUser(
            @PathVariable @NotNull @NotBlank String userId,
            @RequestBody @NotNull @NotEmpty List<@NotEmpty @NotBlank String> roles) {
        try {
            log.debug("Assigning roles {} to user with user ID: {}", roles, userId);
            auth0ManagementService.assignRolesToUser(userId, roles);
            log.info("Successfully assigned roles {} to user with user ID: {}", roles, userId);
            return ResponseEntity.ok("Roles assigned successfully");
        } catch (Auth0ManagementException e) {
            log.error("Error assigning roles to user with user ID: {}", userId, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to assign roles: " + e.getMessage());
        } catch (Exception e) {
            log.error("Error assigning roles to user with user ID: {}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to assign roles: " + e.getMessage());
        }
    }

    /**
     * Removes roles from a user by their user ID.
     *
     * @param userId the user ID of the user to remove roles from.
     * @param roles the list of role IDs to remove.
     * @return a response indicating success or failure.
     * @throws Auth0ManagementException if there is an error removing roles.
     */
    @DeleteMapping("/{userId}/roles")
    public ResponseEntity<String> removeRolesFromUser(
            @PathVariable @NotNull @NotBlank String userId,
            @RequestBody @NotNull @NotEmpty List<@NotEmpty @NotBlank String> roles) {
        try {
            log.debug("Removing roles {} from user with user ID: {}", roles, userId);
            auth0ManagementService.removeRolesFromUser(userId, roles);
            log.info("Successfully removed roles {} from user with user ID: {}", roles, userId);
            return ResponseEntity.ok("Roles removed successfully");
        } catch (Auth0ManagementException e) {
            log.error("Error removing roles from user with user ID: {}", userId, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to remove roles: " + e.getMessage());
        } catch (Exception e) {
            log.error("Error removing roles from user with user ID: {}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to remove roles: " + e.getMessage());
        }
    }
}