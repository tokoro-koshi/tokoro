package com.tokorokoshi.tokoro.configuration;

import com.auth0.client.mgmt.ManagementAPI;
import com.tokorokoshi.tokoro.modules.auth0.Auth0Properties;
import com.tokorokoshi.tokoro.modules.auth0.TokenService;
import com.tokorokoshi.tokoro.modules.exceptions.auth0.Auth0ManagementException;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for setting up Auth0 Management API client.
 * This class provides a Spring-managed bean for the {@link ManagementAPI} which
 * is used to interact with the Auth0 Management API.
 */
@Configuration
@EnableConfigurationProperties(Auth0Properties.class)
public class Auth0Configuration {
    private final Auth0Properties authProperties;
    private final TokenService tokenService;

    /**
     * Constructor for Auth0Configuration.
     *
     * @param auth0Properties properties containing necessary configuration for Auth0.
     * @param tokenService    service responsible for managing and retrieving Auth0 tokens.
     */
    public Auth0Configuration(Auth0Properties auth0Properties, TokenService tokenService) {
        this.authProperties = auth0Properties;
        this.tokenService = tokenService;
    }

    /**
     * Creates and provides a {@link ManagementAPI} bean with a dynamically updated token.
     * The Management API token is retrieved from the {@link TokenService}, which caches it
     * and evicts it after a fixed duration to ensure fresh tokens.
     *
     * @return an instance of {@link ManagementAPI} configured with the domain and token from {@link TokenService}.
     * @throws Auth0ManagementException if there is an error initializing the Management API with the token.
     */
    @Bean
    public ManagementAPI managementAPI() throws Auth0ManagementException {
        // Attempt to retrieve the Management API token from the TokenService
        String managementApiToken = tokenService.getManagementApiToken();

        // Validate the token before using it
        if (managementApiToken == null || managementApiToken.isBlank()) {
            throw new Auth0ManagementException("Retrieved Management API token is null or empty");
        }

        // Create and return a new ManagementAPI instance with the provided token
        return ManagementAPI.newBuilder(
                authProperties.getDomain(),
                managementApiToken
        ).build();
    }
}