package com.tokorokoshi.tokoro.modules.auth0;

import com.auth0.json.mgmt.users.User;
import com.tokorokoshi.tokoro.modules.exceptions.auth0.Auth0ManagementException;
import com.tokorokoshi.tokoro.modules.exceptions.auth0.Auth0UserNotFoundException;
import com.tokorokoshi.tokoro.modules.exceptions.auth0.UserNotAuthenticatedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Service class for handling security-related operations such as retrieving information
 * about the currently authenticated user, their permissions, roles, and metadata.
 * This class also integrates with Auth0 API to manage user-related operations.
 */
@Service
public class Auth0UserDataService {

    private static final Logger log = LoggerFactory.getLogger(Auth0UserDataService.class);
    private static final String PERMISSIONS_CLAIM = "permissions";

    private final Auth0ManagementService auth0ManagementService;

    @Autowired
    public Auth0UserDataService(Auth0ManagementService auth0ManagementService) {
        if (auth0ManagementService == null) {
            throw new IllegalArgumentException("Auth0ManagementService must not be null");
        }
        this.auth0ManagementService = auth0ManagementService;
    }

    /**
     * Retrieves the currently authenticated user's ID from the JWT token.
     *
     * @return the Auth0 user ID of the currently authenticated user.
     * @throws UserNotAuthenticatedException if the user is not authenticated or the token is invalid.
     */
    private String getAuthenticatedUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof Jwt jwt) {
            return jwt.getSubject();
        }
        log.error("User is not authenticated or the token is invalid.");
        throw new UserNotAuthenticatedException("User is not authenticated or the token is invalid.");
    }

    /**
     * Retrieves the currently authenticated user's email.
     *
     * @return the email of the currently authenticated user.
     * @throws UserNotAuthenticatedException if the user is not authenticated or the token is invalid.
     * @throws Auth0ManagementException if there is an error fetching the user from Auth0.
     */
    public String getAuthenticatedUserEmail() {
        String userId = getAuthenticatedUserId();
        User user = auth0ManagementService.getUser(userId);
        return user.getEmail();
    }

    /**
     * Retrieves the currently authenticated user's name.
     *
     * @return the name of the currently authenticated user.
     * @throws UserNotAuthenticatedException if the user is not authenticated or the token is invalid.
     * @throws Auth0ManagementException if there is an error fetching the user from Auth0.
     */
    public String getAuthenticatedUserName() {
        String userId = getAuthenticatedUserId();
        User user = auth0ManagementService.getUser(userId);
        return user.getName();
    }

    /**
     * Retrieves the currently authenticated user's permissions.
     *
     * @return a list of permissions for the currently authenticated user.
     * @throws UserNotAuthenticatedException if the user is not authenticated or the token is invalid.
     */
    public List<String> getAuthenticatedUserPermissions() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof Jwt jwt) {
            return jwt.getClaimAsStringList(PERMISSIONS_CLAIM);
        }
        log.error("User is not authenticated or the token is invalid.");
        throw new UserNotAuthenticatedException("User is not authenticated or the token is invalid.");
    }

    /**
     * Retrieves the currently authenticated user's metadata.
     *
     * @return a map of metadata for the currently authenticated user.
     * @throws UserNotAuthenticatedException if the user is not authenticated or the token is invalid.
     * @throws Auth0ManagementException if there is an error fetching the user from Auth0.
     */
    public Map<String, Object> getAuthenticatedUserMetadata() {
        String userId = getAuthenticatedUserId();
        User user = auth0ManagementService.getUser(userId);
        return user.getUserMetadata();
    }

    /**
     * Retrieves the currently authenticated user's roles.
     *
     * @return a list of roles for the currently authenticated user.
     * @throws UserNotAuthenticatedException if the user is not authenticated or the token is invalid.
     */
    public List<String> getAuthenticatedUserRoles() {
        String userId = getAuthenticatedUserId();
        return auth0ManagementService.getUserRoles(userId);
    }

    /**
     * Retrieves the full user object from Auth0 for the currently authenticated user.
     *
     * @return the {@link User} object representing the currently authenticated user.
     * @throws UserNotAuthenticatedException if the user is not authenticated or the token is invalid.
     * @throws Auth0ManagementException if there is an error fetching the user from Auth0.
     */
    public User getAuthenticatedUserDetails() {
        String userId = getAuthenticatedUserId();
        return auth0ManagementService.getUser(userId);
    }

    /**
     * Checks if the currently authenticated user has a specific permission.
     *
     * @param permission the permission to check.
     * @return true if the user has the permission, false otherwise.
     * @throws UserNotAuthenticatedException if the user is not authenticated or the token is invalid.
     */
    public boolean hasPermission(String permission) {
        List<String> permissions = getAuthenticatedUserPermissions();
        return permissions != null && permissions.contains(permission);
    }

    /**
     * Checks if the currently authenticated user has any of the specified permissions.
     *
     * @param permissions the list of permissions to check.
     * @return true if the user has any of the permissions, false otherwise.
     * @throws UserNotAuthenticatedException if the user is not authenticated or the token is invalid.
     */
    public boolean hasAnyPermission(List<String> permissions) {
        List<String> userPermissions = getAuthenticatedUserPermissions();
        if (userPermissions == null || userPermissions.isEmpty()) {
            return false;
        }
        for (String permission : permissions) {
            if (userPermissions.contains(permission)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the currently authenticated user has a specific role.
     *
     * @param role the role to check.
     * @return true if the user has the role, false otherwise.
     * @throws UserNotAuthenticatedException if the user is not authenticated or the token is invalid.
     */
    public boolean hasRole(String role) {
        List<String> roles = getAuthenticatedUserRoles();
        return roles != null && roles.contains(role);
    }

    /**
     * Checks if the currently authenticated user has any of the specified roles.
     *
     * @param roles the list of roles to check.
     * @return true if the user has any of the roles, false otherwise.
     * @throws UserNotAuthenticatedException if the user is not authenticated or the token is invalid.
     */
    public boolean hasAnyRole(List<String> roles) {
        List<String> userRoles = getAuthenticatedUserRoles();
        if (userRoles == null || userRoles.isEmpty()) {
            return false;
        }
        for (String role : roles) {
            if (userRoles.contains(role)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Fetches a user by their email address.
     *
     * @param email the email address of the user to fetch.
     * @return the {@link User} object representing the user, or null if no user is found.
     * @throws IllegalArgumentException if the email is null or empty.
     * @throws Auth0UserNotFoundException if there is an error fetching the user from Auth0.
     */
    public User getUserByEmail(String email) {
        try {
            List<User> users = auth0ManagementService.searchUsers(String.format("email:%s", email));
            return users.isEmpty() ? null : users.getFirst();
        } catch (Exception e) {
            log.error("Error fetching user with email: {}", email, e);
            throw new Auth0UserNotFoundException("Error fetching user with email: " + email, e);
        }
    }

    /**
     * Updates the metadata for the currently authenticated user.
     *
     * @param metadata a map containing the metadata to be updated.
     * @throws UserNotAuthenticatedException if the user is not authenticated or the token is invalid.
     * @throws IllegalArgumentException if the metadata is null.
     * @throws Auth0ManagementException if there is an error updating the user metadata.
     */
    public void updateAuthenticatedUserMetadata(Map<String, Object> metadata) {
        String userId = getAuthenticatedUserId();
        if (metadata == null) {
            throw new IllegalArgumentException("Metadata must not be null");
        }
        auth0ManagementService.updateUserMetadata(userId, metadata);
    }

    /**
     * Updates the avatar URL for the currently authenticated user.
     *
     * @param avatarUrl the new avatar URL for the user.
     * @throws UserNotAuthenticatedException if the user is not authenticated or the token is invalid.
     * @throws IllegalArgumentException if the avatarUrl is null or empty.
     * @throws Auth0ManagementException if there is an error updating the user avatar.
     */
    public void updateAuthenticatedUserAvatar(String avatarUrl) {
        String userId = getAuthenticatedUserId();
        if (avatarUrl == null || avatarUrl.isBlank()) {
            throw new IllegalArgumentException("Avatar URL must not be null or empty");
        }
        auth0ManagementService.updateUserAvatar(userId, avatarUrl);
    }

    /**
     * Fetches the avatar URL for the currently authenticated user.
     *
     * @return the avatar URL for the authenticated user, or null if the user has no avatar URL set.
     * @throws UserNotAuthenticatedException if the user is not authenticated or the token is invalid.
     * @throws Auth0ManagementException if there is an error fetching the user avatar.
     */
    public String getAuthenticatedUserAvatar() {
        String userId = getAuthenticatedUserId();
        return auth0ManagementService.getUserAvatar(userId);
    }

    /**
     * Updates the name for the currently authenticated user.
     *
     * @param firstName the new first name for the user.
     * @param lastName the new last name for the user.
     * @throws UserNotAuthenticatedException if the user is not authenticated or the token is invalid.
     * @throws IllegalArgumentException if the firstName or lastName is null or empty.
     * @throws Auth0ManagementException if there is an error updating the username.
     */
    public void updateAuthenticatedUserName(String firstName, String lastName) {
        String userId = getAuthenticatedUserId();
        if (firstName == null || firstName.isBlank()) {
            throw new IllegalArgumentException("First name must not be null or empty");
        }
        if (lastName == null || lastName.isBlank()) {
            throw new IllegalArgumentException("Last name must not be null or empty");
        }
        auth0ManagementService.updateUserFirstNameAndLastName(userId, firstName, lastName);
    }

    /**
     * Retrieves the nickname of the currently authenticated user.
     *
     * @return the nickname of the currently authenticated user.
     * @throws UserNotAuthenticatedException if the user is not authenticated or the token is invalid.
     * @throws Auth0ManagementException if there is an error fetching the user nickname.
     */
    public String getAuthenticatedUserNickname() {
        String userId = getAuthenticatedUserId();
        return auth0ManagementService.getUserNickname(userId);
    }

    /**
     * Updates the nickname of the currently authenticated user.
     *
     * @param nickname the new nickname for the user.
     * @throws UserNotAuthenticatedException if the user is not authenticated or the token is invalid.
     * @throws IllegalArgumentException if the nickname is null or empty.
     * @throws Auth0ManagementException if there is an error updating the user nickname.
     */
    public void updateAuthenticatedUserNickname(String nickname) {
        String userId = getAuthenticatedUserId();
        if (nickname == null || nickname.isBlank()) {
            throw new IllegalArgumentException("Nickname must not be null or empty");
        }
        auth0ManagementService.updateUserNickname(userId, nickname);
    }
}

