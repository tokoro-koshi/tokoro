package com.tokorokoshi.tokoro.security;

import com.tokorokoshi.tokoro.modules.exceptions.auth0.UserNotAuthenticatedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

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
        Authentication auth = SecurityContextHolder
                .getContext()
                .getAuthentication();

        // Check if authentication is present and valid
        if (!auth.isAuthenticated()) {
            throw new UserNotAuthenticatedException(
                    "User is not authenticated."
            );
        }

        if (
                auth instanceof JwtAuthenticationToken jwtAuth &&
                        jwtAuth.getPrincipal() instanceof Jwt jwt
        ) {
            return jwt.getSubject();
        } else if (auth.getPrincipal() instanceof User user) {
            return user.getUsername();
        }

        throw new UserNotAuthenticatedException(
                "Authentication token is invalid or not a JWT."
        );
    }
}
