package com.tokorokoshi.tokoro.modules.auth0;

import com.auth0.client.auth.AuthAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Factory class responsible for creating instances of {@link AuthAPI} using the configuration properties
 * provided in the {@link Auth0Properties} class.
 */
@Component
public class AuthAPIFactory {

    private static final Logger log = LoggerFactory.getLogger(AuthAPIFactory.class);

    private final Auth0Properties auth0Properties;

    /**
     * Constructor for AuthAPIFactory.
     *
     * @param auth0Properties properties containing necessary configuration for Auth0.
     */
    public AuthAPIFactory(Auth0Properties auth0Properties) {
        this.auth0Properties = auth0Properties;
    }

    /**
     * Creates and returns a new instance of {@link AuthAPI} using the Auth0 configuration properties.
     *
     * @return a new {@link AuthAPI} instance configured with client ID, client secret, and domain.
     */
    public AuthAPI createAuthAPI() {
        log.info("Creating AuthAPI instance for Auth0 domain: {}", auth0Properties.getDomain());

        // Build and return the AuthAPI instance
        return AuthAPI.newBuilder(
                auth0Properties.getDomain(),
                auth0Properties.getClientId(),
                auth0Properties.getClientSecret()
        ).build();
    }
}