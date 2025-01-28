package com.tokorokoshi.tokoro.modules.auth0;

import com.auth0.client.mgmt.ManagementAPI;
import com.auth0.client.mgmt.filter.RolesFilter;
import com.auth0.client.mgmt.filter.UserFilter;
import com.auth0.exception.Auth0Exception;
import com.auth0.json.mgmt.roles.Role;
import com.auth0.json.mgmt.roles.RolesPage;
import com.auth0.json.mgmt.users.User;
import com.auth0.json.mgmt.users.UsersPage;
import com.tokorokoshi.tokoro.modules.exceptions.auth0.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service class that provides operations for interacting with the Auth0 Management API.
 * Includes methods for fetching, updating, deleting users, and managing user metadata and roles.
 */
@Service
public class Auth0ManagementService {

    private static final Logger log = LoggerFactory.getLogger(Auth0ManagementService.class);

    private final ManagementAPI managementAPI;

    /**
     * Constructs an instance of Auth0ManagementService.
     *
     * @param managementAPI the ManagementAPI instance used for making requests to the Auth0 Management API.
     * @throws IllegalArgumentException if the managementAPI is null.
     */
    public Auth0ManagementService(ManagementAPI managementAPI) {
        if (managementAPI == null) {
            throw new IllegalArgumentException("ManagementAPI must not be null");
        }
        this.managementAPI = managementAPI;
    }

    /**
     * Fetches a user by their Auth0 user ID.
     *
     * @param userId the Auth0 user ID to fetch.
     * @return the {@link User} object representing the Auth0 user.
     * @throws IllegalArgumentException if the userId is null or empty.
     * @throws UserFetchException if there is an error during the fetching process.
     */
    public User getUser(String userId) {
        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("User ID must not be null or empty");
        }
        try {
            log.info("Fetching user with ID: {}", userId);
            return managementAPI.users().get(userId, null).execute().getBody();
        } catch (Auth0Exception e) {
            log.error("Error fetching user with ID: {}", userId, e);
            throw new UserFetchException("Error fetching user with ID: " + userId, e);
        }
    }

    /**
     * Searches for users in Auth0 using a query string.
     *
     * @param query the query string used for searching users (e.g., filter by email, name, etc.).
     * @return a list of {@link User} objects matching the search query.
     * @throws IllegalArgumentException if the query is null or empty.
     * @throws UserSearchException if there is an error during the search process.
     */
    public List<User> searchUsers(String query) {
        if (query == null || query.isEmpty()) {
            throw new IllegalArgumentException("Query must not be null or empty");
        }
        try {
            log.info("Searching users with query: {}", query);
            UsersPage usersPage = managementAPI.users().list(new UserFilter().withQuery(query)).execute().getBody();
            return usersPage.getItems();
        } catch (Auth0Exception e) {
            log.error("Error searching users with query: {}", query, e);
            throw new UserSearchException("Error searching users with query: " + query, e);
        }
    }

    /**
     * Updates the user metadata for a given user.
     *
     * @param userId   the Auth0 user ID of the user whose metadata will be updated.
     * @param metadata a map containing the metadata to be updated.
     * @throws IllegalArgumentException if the userId is null or empty or if metadata is null.
     * @throws UserUpdateException if there is an error during the update process.
     */
    public void updateUserMetadata(String userId, Map<String, Object> metadata) {
        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("User ID must not be null or empty");
        }
        if (metadata == null) {
            throw new IllegalArgumentException("Metadata must not be null");
        }
        try {
            log.info("Updating user metadata for ID: {}", userId);
            User updateRequest = new User();
            updateRequest.setUserMetadata(metadata);
            managementAPI.users().update(userId, updateRequest).execute();
            log.info("Successfully updated metadata for user ID: {}", userId);
        } catch (Auth0Exception e) {
            log.error("Error updating user metadata for ID: {}", userId, e);
            throw new UserUpdateException("Error updating user metadata for ID: " + userId, e);
        }
    }

    /**
     * Deletes a user from Auth0 by their user ID.
     *
     * @param userId the Auth0 user ID of the user to be deleted.
     * @throws IllegalArgumentException if the userId is null or empty.
     * @throws UserDeleteException if there is an error during the deletion process.
     */
    public void deleteUser(String userId) {
        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("User ID must not be null or empty");
        }
        try {
            log.info("Deleting user with ID: {}", userId);
            managementAPI.users().delete(userId).execute();
            log.info("Successfully deleted user with ID: {}", userId);
        } catch (Auth0Exception e) {
            log.error("Error deleting user with ID: {}", userId, e);
            throw new UserDeleteException("Error deleting user with ID: " + userId, e);
        }
    }

    /**
     * Updates the user's name.
     *
     * @param userId    the Auth0 user ID of the user whose name will be updated.
     * @param firstName the new first name for the user.
     * @param lastName  the new last name for the user.
     * @throws IllegalArgumentException if the userId, firstName, or lastName is null or empty.
     * @throws UserUpdateException if there is an error during the update process.
     */
    public void updateUserFirstNameAndLastName(String userId, String firstName, String lastName) {
        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("User ID must not be null or empty");
        }
        if (firstName == null || firstName.isEmpty()) {
            throw new IllegalArgumentException("First name must not be null or empty");
        }
        if (lastName == null || lastName.isEmpty()) {
            throw new IllegalArgumentException("Last name must not be null or empty");
        }
        try {
            log.info("Updating user name for ID: {}", userId);
            User updateRequest = new User();
            updateRequest.setGivenName(firstName);
            updateRequest.setFamilyName(lastName);
            managementAPI.users().update(userId, updateRequest).execute();
            log.info("Successfully updated name for user ID: {}", userId);
        } catch (Auth0Exception e) {
            log.error("Error updating user name for ID: {}", userId, e);
            throw new UserUpdateException("Error updating user name for ID: " + userId, e);
        }
    }

    /**
     * Updates the user's avatar URL.
     *
     * @param userId    the Auth0 user ID of the user whose avatar will be updated.
     * @param avatarUrl the new avatar URL for the user.
     * @throws IllegalArgumentException if the userId or avatarUrl is null or empty.
     * @throws UserUpdateException if there is an error during the update process.
     */
    public void updateUserAvatar(String userId, String avatarUrl) {
        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("User ID must not be null or empty");
        }
        if (avatarUrl == null || avatarUrl.isEmpty()) {
            throw new IllegalArgumentException("Avatar URL must not be null or empty");
        }
        try {
            log.info("Updating user avatar for ID: {}", userId);
            User updateRequest = new User();
            updateRequest.setPicture(avatarUrl);
            managementAPI.users().update(userId, updateRequest).execute();
            log.info("Successfully updated avatar for user ID: {}", userId);
        } catch (Auth0Exception e) {
            log.error("Error updating user avatar for ID: {}", userId, e);
            throw new UserUpdateException("Error updating user avatar for ID: " + userId, e);
        }
    }

    /**
     * Fetches the user's avatar URL.
     *
     * @param userId the Auth0 user ID of the user whose avatar URL will be fetched.
     * @return the avatar URL for the user, or null if the user has no avatar URL set.
     * @throws IllegalArgumentException if the userId is null or empty.
     * @throws UserFetchException if there is an error during the fetch process.
     */
    public String getUserAvatar(String userId) {
        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("User ID must not be null or empty");
        }
        try {
            log.info("Fetching user avatar for ID: {}", userId);
            User user = managementAPI.users().get(userId, null).execute().getBody();
            String avatarUrl = user.getPicture();
            log.info("Successfully fetched avatar for user ID: {}. Avatar URL: {}", userId, avatarUrl);
            return avatarUrl;
        } catch (Auth0Exception e) {
            log.error("Error fetching user avatar for ID: {}", userId, e);
            throw new UserFetchException("Error fetching user avatar for ID: " + userId, e);
        }
    }

    /**
     * Assigns roles to a user.
     *
     * @param userId  the Auth0 user ID of the user to whom roles will be assigned.
     * @param roleIds the list of role IDs to assign to the user.
     * @throws IllegalArgumentException if the userId is null or empty or if roleIds is null or empty.
     * @throws RoleAssignmentException if there is an error during the assignment process.
     */
    public void assignRolesToUser(String userId, List<String> roleIds) {
        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("User ID must not be null or empty");
        }
        if (roleIds == null || roleIds.isEmpty()) {
            throw new IllegalArgumentException("Role IDs must not be null or empty");
        }
        try {
            log.info("Assigning roles to user with ID: {}", userId);
            managementAPI.users().addRoles(userId, roleIds).execute();
            log.info("Successfully assigned roles to user ID: {}", userId);
        } catch (Auth0Exception e) {
            log.error("Error assigning roles to user with ID: {}", userId, e);
            throw new RoleAssignmentException("Error assigning roles to user with ID: " + userId, e);
        }
    }

    /**
     * Removes roles from a user.
     *
     * @param userId  the Auth0 user ID of the user from whom roles will be removed.
     * @param roleIds the list of role IDs to remove from the user.
     * @throws IllegalArgumentException if the userId is null or empty or if roleIds is null or empty.
     * @throws RoleRemovalException if there is an error during the removal process.
     */
    public void removeRolesFromUser(String userId, List<String> roleIds) {
        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("User ID must not be null or empty");
        }
        if (roleIds == null || roleIds.isEmpty()) {
            throw new IllegalArgumentException("Role IDs must not be null or empty");
        }
        try {
            log.info("Removing roles from user with ID: {}", userId);
            managementAPI.users().removeRoles(userId, roleIds).execute();
            log.info("Successfully removed roles from user ID: {}", userId);
        } catch (Auth0Exception e) {
            log.error("Error removing roles from user with ID: {}", userId, e);
            throw new RoleRemovalException("Error removing roles from user with ID: " + userId, e);
        }
    }

    /**
     * Fetches roles assigned to a user.
     *
     * @param userId the Auth0 user ID of the user whose roles will be fetched.
     * @return a list of {@link Role} objects representing the roles assigned to the user.
     * @throws IllegalArgumentException if the userId is null or empty.
     * @throws RoleFetchException if there is an error during the fetching process.
     */
    public List<String> getUserRoles(String userId) {
        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("User ID must not be null or empty");
        }
        try {
            log.info("Fetching roles for user with ID: {}", userId);
            RolesPage rolesPage = managementAPI.users().listRoles(userId, new RolesFilter()).execute().getBody();
            return rolesPage.getItems().stream().map(Role::getName).collect(Collectors.toList());
        } catch (Auth0Exception e) {
            log.error("Error fetching roles for user with ID: {}", userId, e);
            throw new RoleFetchException("Error fetching roles for user with ID: " + userId, e);
        }
    }

    /**
     * Fetches all roles available in the Auth0 tenant.
     *
     * @return a list of {@link Role} objects representing all roles in the tenant.
     * @throws RoleFetchException if there is an error during the fetching process.
     */
    public List<String> getAllRoles() {
        try {
            log.info("Fetching all roles");
            RolesPage rolesPage = managementAPI.roles().list(new RolesFilter()).execute().getBody();
            return rolesPage.getItems().stream().map(Role::getName).collect(Collectors.toList());
        } catch (Auth0Exception e) {
            log.error("Error fetching all roles", e);
            throw new RoleFetchException("Error fetching all roles", e);
        }
    }

    /**
     * Checks if the user is blocked.
     *
     * @param userId the Auth0 user ID of the user to check if they are blocked.
     * @return true if the user is blocked, false otherwise.
     * @throws UserNotAuthenticatedException if the user is not authenticated or the token is invalid.
     * @throws Auth0ManagementException if there is an error fetching the user from Auth0.
     */
    public boolean isUserBlocked(String userId) {
        try {
            User user = managementAPI.users().get(userId, new UserFilter()).execute().getBody();
            boolean isBlocked = user.isBlocked() != null ? user.isBlocked() : false;
            log.info("User with ID {} is blocked: {}", userId, isBlocked);
            return isBlocked;
        } catch (Auth0Exception e) {
            log.error("Error fetching user with ID {}", userId, e);
            throw new Auth0ManagementException("Failed to fetch user with ID " + userId, e);
        } catch (Exception e) {
            log.error("Unexpected error checking if user with ID {} is blocked", userId, e);
            throw new Auth0ManagementException("Unexpected error checking if user with ID " + userId + " is blocked", e);
        }
    }

    /**
     * Blocks a user.
     *
     * @param userId the Auth0 user ID of the user to be blocked.
     * @throws IllegalArgumentException if the userId is null or empty.
     * @throws UserUpdateException if there is an error during the blocking process.
     */
    public void blockUser(String userId) {
        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("User ID must not be null or empty");
        }
        try {
            log.info("Blocking user with ID: {}", userId);
            User updateRequest = new User();
            updateRequest.setBlocked(true);
            managementAPI.users().update(userId, updateRequest).execute();
            log.info("Successfully blocked user with ID: {}", userId);
        } catch (Auth0Exception e) {
            log.error("Error blocking user with ID: {}", userId, e);
            throw new UserUpdateException("Error blocking user with ID: " + userId, e);
        }
    }

    /**
     * Unblocks a user.
     *
     * @param userId the Auth0 user ID of the user to be unblocked.
     * @throws IllegalArgumentException if the userId is null or empty.
     * @throws UserUpdateException if there is an error during the unblocking process.
     */
    public void unblockUser(String userId) {
        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("User ID must not be null or empty");
        }
        try {
            log.info("Unblocking user with ID: {}", userId);
            User updateRequest = new User();
            updateRequest.setBlocked(false);
            managementAPI.users().update(userId, updateRequest).execute();
            log.info("Successfully unblocked user with ID: {}", userId);
        } catch (Auth0Exception e) {
            log.error("Error unblocking user with ID: {}", userId, e);
            throw new UserUpdateException("Error unblocking user with ID: " + userId, e);
        }
    }

    /**
     * Retrieves the nickname of the user by their user ID.
     *
     * @param userId the Auth0 user ID of the user.
     * @return the nickname of the user.
     * @throws IllegalArgumentException if the userId is null or empty.
     * @throws UserFetchException if there is an error during the fetching process.
     */
    public String getUserNickname(String userId) {
        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("User ID must not be null or empty");
        }
        try {
            log.info("Fetching nickname for user with ID: {}", userId);
            User user = managementAPI.users().get(userId, null).execute().getBody();
            return user.getNickname();
        } catch (Auth0Exception e) {
            log.error("Error fetching nickname for user with ID: {}", userId, e);
            throw new UserFetchException("Error fetching nickname for user with ID: " + userId, e);
        }
    }

    /**
     * Updates the nickname of the user by their user ID.
     *
     * @param userId   the Auth0 user ID of the user.
     * @param nickname the new nickname for the user.
     * @throws IllegalArgumentException if the userId or nickname is null or empty.
     * @throws UserUpdateException if there is an error during the update process.
     */
    public void updateUserNickname(String userId, String nickname) {
        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("User ID must not be null or empty");
        }
        if (nickname == null || nickname.isEmpty()) {
            throw new IllegalArgumentException("Nickname must not be null or empty");
        }
        try {
            log.info("Updating nickname for user with ID: {}", userId);
            User updateRequest = new User();
            updateRequest.setNickname(nickname);
            managementAPI.users().update(userId, updateRequest).execute();
            log.info("Successfully updated nickname for user ID: {}", userId);
        } catch (Auth0Exception e) {
            log.error("Error updating nickname for user with ID: {}", userId, e);
            throw new UserUpdateException("Error updating nickname for user with ID: " + userId, e);
        }
    }
}