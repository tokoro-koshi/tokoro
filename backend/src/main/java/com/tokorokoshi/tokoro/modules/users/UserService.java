package com.tokorokoshi.tokoro.modules.users;

import com.auth0.json.mgmt.permissions.Permission;
import com.auth0.json.mgmt.roles.Role;
import com.auth0.json.mgmt.users.User;
import com.tokorokoshi.tokoro.modules.auth0.Auth0ManagementService;
import com.tokorokoshi.tokoro.modules.exceptions.auth0.*;
import com.tokorokoshi.tokoro.modules.file.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    private final Auth0ManagementService auth0ManagementService;
    private final FileStorageService fileStorageService;

    @Autowired
    public UserService(Auth0ManagementService auth0ManagementService, FileStorageService fileStorageService) {
        this.auth0ManagementService = auth0ManagementService;
        this.fileStorageService = fileStorageService;
    }

    /**
     * Fetches a user by their Auth0 user ID.
     *
     * @param userId the Auth0 user ID to fetch.
     * @return the {@link User} object representing the Auth0 user.
     * @throws UserFetchException if there is an error during the fetching process.
     */
    public User getUser(String userId) {
        return auth0ManagementService.getUser(userId);
    }

    /**
     * Fetches the user's email address.
     *
     * @param userId the Auth0 user ID of the user whose email will be fetched.
     * @return the email address for the user.
     * @throws UserFetchException if there is an error during the fetching process.
     */
    public String getUserEmail(String userId) {
        User user = auth0ManagementService.getUser(userId);
        return user.getEmail();
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
     * @param userId the Auth0 user ID of the user whose avatar will be updated.
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
     * Fetches the user's avatar URL.
     *
     * @param userId the Auth0 user ID of the user whose avatar URL will be fetched.
     * @return the avatar URL for the user, or null if the user has no avatar URL set.
     * @throws UserFetchException if there is an error during the fetch process.
     */
    public String getUserAvatar(String userId) {
        return auth0ManagementService.getUserAvatar(userId);
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
     * Retrieves the nickname of the user by their user ID.
     *
     * @param userId the Auth0 user ID of the user.
     * @return the nickname of the user.
     * @throws UserFetchException if there is an error during the fetching process.
     */
    public String getUserNickname(String userId) {
        return auth0ManagementService.getUserNickname(userId);
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
     * @param userId the Auth0 user ID of the user.
     * @param roleIds the list of role IDs to remove from the user.
     * @throws UserUpdateException if there is an error during the update process.
     */
    public void removeRolesFromUser(String userId, List<String> roleIds) {
        auth0ManagementService.removeRolesFromUser(userId, roleIds);
    }

    /**
     * Assigns roles to a user.
     *
     * @param userId the Auth0 user ID of the user.
     * @param roleIds the list of role IDs to assign to the user.
     * @throws UserUpdateException if there is an error during the update process.
     */
    public void assignRolesToUser(String userId, List<String> roleIds) {
        auth0ManagementService.assignRolesToUser(userId, roleIds);
    }

    /**
     * Fetches the roles assigned to a user.
     *
     * @param userId the Auth0 user ID of the user.
     * @return the list of roles assigned to the user.
     * @throws UserFetchException if there is an error during the fetching process.
     */
    public List<Role> getUserRoles(String userId) {
        return auth0ManagementService.getUserRoles(userId);
    }

    /**
     * Fetches the permissions assigned to a user.
     *
     * @param userId the Auth0 user ID of the user.
     * @return the list of permissions assigned to the user.
     * @throws UserFetchException if there is an error during the fetching process.
     */
    public List<Permission> getUserPermissions(String userId) {
        return auth0ManagementService.getUserPermissions(userId);
    }
}
