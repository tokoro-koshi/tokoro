package com.tokorokoshi.tokoro.modules.users;

import com.auth0.json.mgmt.permissions.Permission;
import com.auth0.json.mgmt.roles.Role;
import com.auth0.json.mgmt.users.User;
import com.tokorokoshi.tokoro.modules.error.NotFoundException;
import com.tokorokoshi.tokoro.modules.exceptions.auth0.UserDeleteException;
import com.tokorokoshi.tokoro.modules.exceptions.auth0.UserFetchException;
import com.tokorokoshi.tokoro.modules.exceptions.auth0.UserUpdateException;
import com.tokorokoshi.tokoro.security.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Tag(name = "Users", description = "API for managing users")
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
            summary = "Update user avatar",
            description = "Updates the avatar URL for the user"
    )
    @PostMapping(
            path = "/{id}/avatar",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Void> updateUserAvatar(
            @Parameter(
                    description = "The user ID",
                    required = true,
                    example = "auth0|60f1b3b3b3b3b3b3b3b3b3b"
            )
            @PathVariable
            String id,
            @Parameter(
                    description = "The avatar file to update",
                    required = true
            )
            @RequestParam("file")
            MultipartFile avatarFile
    ) {
        try {
            userService.updateUserAvatar(id, avatarFile);
            return ResponseEntity.noContent().build();
        } catch (UserUpdateException ex) {
            throw new NotFoundException("User not found");
        }
    }

    @Operation(
            summary = "Update user metadata",
            description = "Updates the metadata for the user"
    )
    @PatchMapping(
            path = "/{id}/metadata",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Void> updateUserMetadata(
            @Parameter(
                    description = "The user ID",
                    required = true,
                    example = "auth0|60f1b3b3b3b3b3b3b3b3b3b"
            )
            @PathVariable
            String id,
            @Parameter(
                    description = "The metadata to update",
                    required = true
            )
            @RequestBody
            Map<String, Object> metadata
    ) {
        try {
            userService.updateUserMetadata(id, metadata);
            return ResponseEntity.noContent().build();
        } catch (UserUpdateException e) {
            throw new NotFoundException("User metadata not found");
        }
    }

    @Operation(
            summary = "Delete user",
            description = "Deletes the user from Auth0"
    )
    @DeleteMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(
            @Parameter(
                    description = "The user ID",
                    required = true,
                    example = "auth0|60f1b3b3b3b3b3b3b3b3b3b"
            )
            @PathVariable
            String id
    ) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (UserDeleteException e) {
            throw new NotFoundException("User not found");
        }
    }

    @Operation(
            summary = "Update user name",
            description = "Updates the first and last name of the user"
    )
    @PatchMapping(
            path = "/{id}/name",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updateUserFirstNameAndLastName(
            @Parameter(
                    description = "The user ID",
                    required = true,
                    example = "auth0|60f1b3b3b3b3b3b3b3b3b3b"
            )
            @PathVariable
            String id,
            @Parameter(
                    description = "The first name and last name to update",
                    required = true
            )
            @RequestBody
            Map<String, String> names
    ) {
        try {
            String firstName = names.get("firstName");
            String lastName = names.get("lastName");
            userService.updateUserFirstNameAndLastName(id, firstName, lastName);
            return ResponseEntity.noContent().build();
        } catch (UserUpdateException e) {
            throw new NotFoundException("User not found");
        }
    }

    @Operation(
            summary = "Update user nickname",
            description = "Updates the nickname of the user"
    )
    @PatchMapping(
            path = "/{id}/nickname",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Void> updateUserNickname(
            @Parameter(
                    description = "The user ID",
                    required = true,
                    example = "auth0|60f1b3b3b3b3b3b3b3b3b3b"
            )
            @PathVariable
            String id,
            @Parameter(
                    description = "The nickname to update",
                    required = true
            )
            @RequestBody
            Map<String, String> nicknameMap
    ) {
        try {
            String nickname = nicknameMap.get("nickname");
            userService.updateUserNickname(id, nickname);
            return ResponseEntity.noContent().build();
        } catch (UserUpdateException e) {
            throw new NotFoundException("User not found");
        }
    }

    @Operation(
            summary = "Block user",
            description = "Blocks the user from logging in"
    )
    @PostMapping(
            path = "/{id}/block",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> blockUser(
            @Parameter(
                    description = "The user ID",
                    required = true,
                    example = "auth0|60f1b3b3b3b3b3b3b3b3b3b"
            )
            @PathVariable
            String id
    ) {
        try {
            userService.blockUser(id);
            return ResponseEntity.noContent().build();
        } catch (UserUpdateException e) {
            throw new NotFoundException("User not found");
        }
    }

    @Operation(
            summary = "Unblock user",
            description = "Unblocks the user, allowing them to log in"
    )
    @PostMapping(
            path = "/{id}/unblock",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> unblockUser(
            @Parameter(
                    description = "The user ID",
                    required = true,
                    example = "auth0|60f1b3b3b3b3b3b3b3b3b3b"
            )
            @PathVariable
            String id
    ) {
        try {
            userService.unblockUser(id);
            return ResponseEntity.noContent().build();
        } catch (UserUpdateException e) {
            throw new NotFoundException("User not found");
        }
    }

    @Operation(
            summary = "Check if user is blocked",
            description = "Checks if the user is blocked"
    )
    @GetMapping(
            path = "/{id}/is-blocked",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Boolean> isUserBlocked(
            @Parameter(
                    description = "The user ID",
                    required = true,
                    example = "auth0|60f1b3b3b3b3b3b3b3b3b3b"
            )
            @PathVariable
            String id
    ) {
        try {
            Boolean isBlocked = userService.isUserBlocked(id);
            return ResponseEntity.ok(isBlocked);
        } catch (UserFetchException e) {
            throw new NotFoundException("User not found");
        }
    }

    @Operation(
            summary = "Get user permissions",
            description = "Returns the list of permissions for the user"
    )
    @GetMapping(
            path = "/{id}/permissions",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Permission>> getUserPermissions(
            @Parameter(
                    description = "The user ID",
                    required = true,
                    example = "auth0|60f1b3b3b3b3b3b3b3b3b3b"
            )
            @PathVariable
            String id
    ) {
        try {
            List<Permission> permissions = userService.getUserPermissions(id);
            return ResponseEntity.ok(permissions);
        } catch (UserFetchException e) {
            throw new NotFoundException("User permissions not found");
        }
    }

    @Operation(
            summary = "Get user roles",
            description = "Returns the list of roles for the user"
    )
    @GetMapping(
            path = "/{id}/roles",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Role>> getUserRoles(
            @Parameter(
                    description = "The user ID",
                    required = true,
                    example = "auth0|60f1b3b3b3b3b3b3b3b3b3b"
            )
            @PathVariable
            String id
    ) {
        try {
            List<Role> roles = userService.getUserRoles(id);
            return ResponseEntity.ok(roles);
        } catch (UserFetchException e) {
            throw new NotFoundException("User roles not found");
        }
    }

    @Operation(
            summary = "Get user details for authenticated user",
            description = "Returns the details of the current user"
    )
    @GetMapping(
            path = "/me",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<User> getUserDetails() {
        try {
            User userDetails = userService.getUser(SecurityUtils.getAuthenticatedUserId());
            return ResponseEntity.ok(userDetails);
        } catch (UserFetchException e) {
            throw new NotFoundException("User not found");
        }
    }

    @Operation(
            summary = "Get user details",
            description = "Returns the details of the user"
    )
    @GetMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<User> getUserDetails(
            @Parameter(
                    description = "The user ID",
                    required = true,
                    example = "auth0|60f1b3b3b3b3b3b3b3b3b3b"
            )
            @PathVariable
            String id
    ) {
        try {
            User userDetails = userService.getUser(id);
            return ResponseEntity.ok(userDetails);
        } catch (UserFetchException e) {
            throw new NotFoundException("User not found");
        }
    }

    @Operation(
            summary = "Assign roles to user",
            description = "Assigns the specified roles to the user"
    )
    @PostMapping(
            path = "/{id}/roles",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> assignRolesToUser(
            @Parameter(
                    description = "The user ID",
                    required = true,
                    example = "auth0|60f1b3b3b3b3b3b3b3b3b3b"
            )
            @PathVariable
            String id,
            @Parameter(
                    description = "The list of roles to assign",
                    required = true,
                    example = "[\"admin\", \"user\"]"
            )
            @RequestBody
            List<String> roles
    ) {
        try {
            userService.assignRolesToUser(id, roles);
            return ResponseEntity.noContent().build();
        } catch (UserUpdateException e) {
            throw new NotFoundException("User not found");
        }
    }

    @Operation(
            summary = "Remove roles from user",
            description = "Removes the specified roles from the user"
    )
    @DeleteMapping(
            path = "/{id}/roles",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Void> removeRolesFromUser(
            @Parameter(
                    description = "The user ID",
                    required = true,
                    example = "auth0|60f1b3b3b3b3b3b3b3b3b3b"
            )
            @PathVariable
            String id,
            @Parameter(
                    description = "The list of roles to remove",
                    required = true,
                    example = "[\"admin\", \"user\"]"
            )
            @RequestBody
            List<String> roles
    ) {
        try {
            userService.removeRolesFromUser(id, roles);
            return ResponseEntity.noContent().build();
        } catch (UserUpdateException e) {
            throw new NotFoundException("User roles not found");
        }
    }
}
