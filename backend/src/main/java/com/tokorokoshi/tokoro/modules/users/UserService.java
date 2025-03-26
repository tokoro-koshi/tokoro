package com.tokorokoshi.tokoro.modules.users;

import com.auth0.json.mgmt.users.User;
import com.tokorokoshi.tokoro.modules.auth0.Auth0ManagementService;
import com.tokorokoshi.tokoro.modules.exceptions.auth0.Auth0ManagementException;
import com.tokorokoshi.tokoro.modules.exceptions.auth0.UserDeleteException;
import com.tokorokoshi.tokoro.modules.exceptions.auth0.UserFetchException;
import com.tokorokoshi.tokoro.modules.exceptions.auth0.UserUpdateException;
import com.tokorokoshi.tokoro.modules.file.FileStorageService;
import com.tokorokoshi.tokoro.modules.users.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {
    private final Auth0ManagementService auth0ManagementService;
    private final FileStorageService fileStorageService;
    private final UserMapper userMapper;

    @Autowired
    public UserService(
            Auth0ManagementService auth0ManagementService,
            FileStorageService fileStorageService,
            UserMapper userMapper
    ) {
        this.auth0ManagementService = auth0ManagementService;
        this.fileStorageService = fileStorageService;
        this.userMapper = userMapper;
    }

    /**
     * Fetches a user by their Auth0 user ID.
     *
     * @param userId the Auth0 user ID to fetch.
     * @return the {@link User} object representing the Auth0 user.
     * @throws UserFetchException if there is an error during the fetching process.
     */
    public UserDto getUser(String userId) {
        return userMapper
                .toDto(auth0ManagementService.getUser(userId))
                .withRoles(auth0ManagementService.getUserRoles(userId))
//                .withPermissions(auth0ManagementService.getUserPermissions(userId));
                .withPermissions(List.of());
    }

    /**
     * Fetches all users
     *
     * @throws UserFetchException if there is an error during the fetching process.
     */
    public Page<UserDto> getUsers(Pageable pageable) {
        List<UserDto> users = auth0ManagementService.getUsers(pageable).stream()
                .map(user -> userMapper.toDto(user)
                        .withRoles(auth0ManagementService.getUserRoles(user.getId()))
//                        .withPermissions(auth0ManagementService.getUserPermissions(user.getId()))
                        .withPermissions(List.of())
                )
                .toList();
        return new PageImpl<>(users, pageable, users.size());
    }

    /**
     * Updates the user metadata for a given user.
     *
     * @param userId   the Auth0 user ID of the user whose metadata will be updated.
     * @param metadata a map containing the metadata to be updated.
     * @throws UserUpdateException if there is an error during the update process.
     */
    public void updateUserMetadata(String userId, Map<String, Object> metadata) {
        auth0ManagementService.updateUserMetadata(userId, metadata);
    }

    /**
     * Deletes a user from Auth0 by their user ID.
     *
     * @param userId the Auth0 user ID of the user to be deleted.
     * @throws UserDeleteException if there is an error during the deletion process.
     */
    public void deleteUser(String userId) {
        auth0ManagementService.deleteUser(userId);
    }

    /**
     * Updates the user's name.
     *
     * @param userId    the Auth0 user ID of the user whose name will be updated.
     * @param firstName the new first name for the user.
     * @param lastName  the new last name for the user.
     * @throws UserUpdateException if there is an error during the update process.
     */
    public void updateUserFirstNameAndLastName(String userId, String firstName, String lastName) {
        auth0ManagementService.updateUserFirstNameAndLastName(userId, firstName, lastName);
    }

    /**
     * Uploads a new avatar for the user, deletes the previous avatar if it exists,
     * and updates the user's avatar URL in Auth0.
     *
     * @param userId     the Auth0 user ID of the user whose avatar will be updated.
     * @param avatarFile the avatar file to upload.
     * @throws UserUpdateException if there is an error during the update process.
     */
    public void updateUserAvatar(String userId, MultipartFile avatarFile) {
        if (avatarFile == null || avatarFile.isEmpty()) {
            throw new IllegalArgumentException("Avatar file cannot be null or empty");
        }

        String mimeType = avatarFile.getContentType();
        if (mimeType == null || !mimeType.startsWith("image/")) {
            throw new IllegalArgumentException("Invalid file type. Only image files are allowed.");
        }

        // Retrieve the existing avatar key from user metadata
        User user = auth0ManagementService.getUser(userId);
        Map<String, Object> userMetadata = user.getUserMetadata();
        String existingAvatarKey = (String) userMetadata.get("avatar_key");

        // Delete the existing avatar if it exists
        if (existingAvatarKey != null) {
            fileStorageService.deleteFile(existingAvatarKey).join();
        }

        // Upload the new avatar file
        String newAvatarKey = fileStorageService.uploadFile(avatarFile, "avatars").join();
        String newAvatarUrl = fileStorageService.generateSignedUrl(newAvatarKey).join();

        // Store the new avatar key in user metadata
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("avatar_key", newAvatarKey);

        // Update user metadata with the new avatar key
        updateUserMetadata(userId, metadata);

        // Update the user's avatar URL in Auth0
        auth0ManagementService.updateUserAvatar(userId, newAvatarUrl);
    }

    /**
     * Checks if the user is blocked.
     *
     * @param userId the Auth0 user ID of the user to check if they are blocked.
     * @return true if the user is blocked, false otherwise.
     * @throws Auth0ManagementException if there is an error fetching the user from Auth0.
     */
    public Boolean isUserBlocked(String userId) {
        return auth0ManagementService.isUserBlocked(userId);
    }

    /**
     * Blocks a user.
     *
     * @param userId the Auth0 user ID of the user to be blocked.
     * @throws UserUpdateException if there is an error during the blocking process.
     */
    public void blockUser(String userId) {
        auth0ManagementService.blockUser(userId);
    }

    /**
     * Unblocks a user.
     *
     * @param userId the Auth0 user ID of the user to be unblocked.
     * @throws UserUpdateException if there is an error during the unblocking process.
     */
    public void unblockUser(String userId) {
        auth0ManagementService.unblockUser(userId);
    }

    /**
     * Fetches the user's metadata.
     *
     * @param userId the Auth0 user ID of the user.
     * @return a map containing the user's metadata.
     * @throws UserFetchException if there is an error during the fetching process.
     */
    public Map<String, Object> getUserMetadata(String userId) {
        User user = auth0ManagementService.getUser(userId);
        return user.getUserMetadata();
    }

    /**
     * Updates the nickname of the user by their user ID.
     *
     * @param userId   the Auth0 user ID of the user.
     * @param nickname the new nickname for the user.
     * @throws UserUpdateException if there is an error during the update process.
     */
    public void updateUserNickname(String userId, String nickname) {
        auth0ManagementService.updateUserNickname(userId, nickname);
    }

    /**
     * Removes roles from a user.
     *
     * @param userId  the Auth0 user ID of the user.
     * @param roleIds the list of role IDs to remove from the user.
     * @throws UserUpdateException if there is an error during the update process.
     */
    public void removeRolesFromUser(String userId, List<String> roleIds) {
        auth0ManagementService.removeRolesFromUser(userId, roleIds);
    }

    /**
     * Assigns roles to a user.
     *
     * @param userId  the Auth0 user ID of the user.
     * @param roles the list of role IDs to assign to the user.
     * @throws UserUpdateException if there is an error during the update process.
     */
    public void assignRolesToUser(String userId, List<String> roles) {
        auth0ManagementService.assignRolesToUser(userId, roles);
    }
}
