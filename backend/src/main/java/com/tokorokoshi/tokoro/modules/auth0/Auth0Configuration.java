package com.tokorokoshi.tokoro.modules.auth0;

import com.auth0.client.mgmt.ManagementAPI;
import com.tokorokoshi.tokoro.modules.exceptions.auth0.Auth0ManagementException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(Auth0Configuration.class);

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
        try {
            // Attempt to retrieve the Management API token from the TokenService
            String managementApiToken = tokenService.getManagementApiToken();
            log.debug("Successfully retrieved Management API token");

            // Validate the token before using it
            if (managementApiToken == null || managementApiToken.isEmpty()) {
                throw new Auth0ManagementException("Retrieved Management API token is null or empty");
            }

            // Create and return a new ManagementAPI instance with the provided token
            ManagementAPI managementAPI = ManagementAPI.newBuilder(
                    authProperties.getDomain(),
                    managementApiToken
            ).build();

            log.info("ManagementAPI initialized successfully with domain: {}", authProperties.getDomain());
            return managementAPI;

        } catch (Exception e) {
            log.error("Unexpected error initializing ManagementAPI with the retrieved token", e);
            throw new Auth0ManagementException("Failed to initialize Management API with token due to unexpected error", e);
        }
    }
}