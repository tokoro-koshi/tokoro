package com.tokorokoshi.tokoro.modules.auth0;

import com.auth0.client.auth.AuthAPI;
import com.auth0.exception.Auth0Exception;
import com.auth0.json.auth.TokenHolder;
import com.tokorokoshi.tokoro.modules.exceptions.auth0.Auth0ManagementException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Service class responsible for managing and retrieving Auth0 tokens.
 * This class provides methods to fetch and cache the Management API token,
 * with retry mechanisms and scheduled cache eviction.
 */
@Service
@EnableRetry // Enable retry functionality in the service
public class TokenService {

    private static final Logger log = LoggerFactory.getLogger(TokenService.class);

    private final AuthAPIFactory authAPIFactory;
    private final Auth0Properties auth0Properties;

    /**
     * Constructor for TokenService.
     *
     * @param authAPIFactory factory for creating AuthAPI instances.
     * @param auth0Properties properties containing necessary configuration for Auth0.
     */
    public TokenService(AuthAPIFactory authAPIFactory, Auth0Properties auth0Properties) {
        this.authAPIFactory = authAPIFactory;
        this.auth0Properties = auth0Properties;
    }

    /**
     * Fetches the Auth0 Management API token, caches it, and retries on failure.
     *
     * @return the Management API token.
     * @throws Auth0ManagementException if there is an error obtaining the token.
     */
    @Retryable(
            retryFor = {Auth0Exception.class},
            backoff = @Backoff(delay = 2000, multiplier = 2)
    )
    @Cacheable(value = "auth0ManagementToken", unless = "#result == null")
    public String getManagementApiToken() throws Auth0ManagementException {
        try {
            // Create an AuthAPI instance
            AuthAPI authAPI = authAPIFactory.createAuthAPI();

            // Request a token for the Management API audience
            TokenHolder holder = authAPI
                    .requestToken(auth0Properties.getManagementApiAudience())
                    .execute()
                    .getBody();

            // Validate the token holder and access token
            if (holder == null || holder.getAccessToken() == null) {
                log.error("Failed to obtain Management API token: holder or access token is null");
                throw new Auth0ManagementException("Failed to obtain Management API token");
            }

            return holder.getAccessToken();

        } catch (Auth0Exception e) {
            log.error("Error obtaining Management API token due to Auth0 exception", e);
            throw new Auth0ManagementException("Failed to obtain Management API token due to Auth0 exception", e);
        }
    }

    /**
     * Evicts the cached Management API token once every 23 hours.
     * This ensures that the token is refreshed periodically.
     */
    @Scheduled(fixedRate = 23 * 60 * 60 * 1000) // 23 hours
    @CacheEvict(value = "auth0ManagementToken", allEntries = true)
    public void evictManagementTokenCache() {
    }
}