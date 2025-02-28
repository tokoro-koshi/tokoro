package com.tokorokoshi.tokoro.security;

import com.tokorokoshi.tokoro.modules.exceptions.auth0.UserNotAuthenticatedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

/**
 * Utility class for security-related operations, specifically for retrieving
 * the authenticated user's ID from the JWT token.
 */
public class SecurityUtils {
    /**
     * Retrieves the currently authenticated user's ID from the JWT token.
     *
     * @return the Auth0 user ID of the currently authenticated user.
     * @throws UserNotAuthenticatedException if the user is not authenticated or the token is invalid.
     */
    public static String getAuthenticatedUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof Jwt jwt) {
            return jwt.getSubject();
        }
        throw new UserNotAuthenticatedException("User is not authenticated or the token is invalid.");
    }
}
