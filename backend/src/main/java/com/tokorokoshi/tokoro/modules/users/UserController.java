package com.tokorokoshi.tokoro.modules.users;

import com.auth0.json.mgmt.users.User;
import com.tokorokoshi.tokoro.modules.error.NotFoundException;
import com.tokorokoshi.tokoro.modules.exceptions.auth0.UserDeleteException;
import com.tokorokoshi.tokoro.modules.exceptions.auth0.UserFetchException;
import com.tokorokoshi.tokoro.modules.exceptions.auth0.UserUpdateException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
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
            summary = "Get user email",
            description = "Returns the email of the user"
    )
    @GetMapping(
            path = "/{id}/email",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> getUserEmail(
            @Parameter(
                    description = "The user ID",
                    required = true,
                    example = "auth0|60f1b3b3b3b3b3b3b3b3b3b"
            )
            @PathVariable
            String id
    ) {
        try {
            String email = userService.getUserEmail(id);
            return ResponseEntity.ok(email);
        } catch (UserFetchException ex) {
            throw new NotFoundException("User email not found");
        }
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
    public ResponseEntity<String> updateUserAvatar(
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
            return ResponseEntity.ok("User avatar updated successfully");
        } catch (UserUpdateException ex) {
            throw new NotFoundException("User not found");
        }
    }

    @Operation(
            summary = "Get user avatar",
            description = "Returns the avatar URL of the user")
    @GetMapping(
            path = "/{id}/avatar",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> getUserAvatar(
            @Parameter(
                    description = "The user ID",
                    required = true,
                    example = "auth0|60f1b3b3b3b3b3b3b3b3b3b"
            )
            @PathVariable
            String id
    ) {
        try {
            String avatarUrl = userService.getUserAvatar(id);
            return ResponseEntity.ok(avatarUrl);
        } catch (UserFetchException e) {
            throw new NotFoundException("User avatar not found");
        }
    }

    @Operation(
            summary = "Get user nickname",
            description = "Returns the nickname of the user")
    @GetMapping(
            path = "/{id}/nickname",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> getUserNickname(
            @Parameter(
                    description = "The user ID",
                    required = true,
                    example = "auth0|60f1b3b3b3b3b3b3b3b3b3b"
            )
            @PathVariable
            String id
    ) {
        try {
            String userNickname = userService.getUserNickname(id);
            return ResponseEntity.ok(userNickname);
        } catch (UserFetchException e) {
            throw new NotFoundException("User nickname not found");
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
    public ResponseEntity<String> updateUserMetadata(
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
            return ResponseEntity.ok("User metadata updated successfully");
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
    public ResponseEntity<String> deleteUser(
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
            return ResponseEntity.ok("User deleted successfully");
        } catch (UserDeleteException e) {
            throw new NotFoundException("User not found");
        }
    }

    @Operation(
            summary = "Get user name",
            description = "Returns the first and last name of the user"
    )
    @GetMapping(
            path = "/{id}/name",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Map<String, String>> getUserName(
            @Parameter(
                    description = "The user ID",
                    required = true,
                    example = "auth0|60f1b3b3b3b3b3b3b3b3b3b"
            )
            @PathVariable
            String id
    ) {
        try {
            User user = userService.getUser(id);
            Map<String, String> userName = new HashMap<>();
            userName.put("firstName", user.getGivenName());
            userName.put("lastName", user.getFamilyName());
            return ResponseEntity.ok(userName);
        } catch (UserFetchException e) {
            throw new NotFoundException("User name not found");
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
    public ResponseEntity<String> updateUserFirstNameAndLastName(
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
            return ResponseEntity.ok("User name updated successfully");
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
    public ResponseEntity<String> updateUserNickname(
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
            return ResponseEntity.ok("User nickname updated successfully");
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
    public ResponseEntity<String> blockUser(
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
            return ResponseEntity.ok("User blocked successfully");
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
    public ResponseEntity<String> unblockUser(
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
            return ResponseEntity.ok("User unblocked successfully");
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
}
