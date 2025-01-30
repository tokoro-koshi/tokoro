package com.tokorokoshi.tokoro.modules.auth0;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;

/**
 * Configuration properties class for Auth0 settings.
 * This class holds the necessary configuration properties required to interact with the Auth0 API.
 */
@Validated
@ConfigurationProperties(prefix = "auth0")
public class Auth0Properties {
    @NotBlank(message = "Auth0 domain is required")
    private String domain;

    @NotBlank(message = "Auth0 client ID is required")
    private String clientId;

    @NotBlank(message = "Auth0 client secret is required")
    private String clientSecret;

    /**
     * Returns the Management API audience URL.
     *
     * @return the Management API audience URL.
     */
    public String getManagementApiAudience() {
        return "https://" + domain + "/api/v2/";
    }

    /**
     * Returns the Auth0 domain.
     *
     * @return the Auth0 domain.
     */
    public String getDomain() {
        return domain;
    }

    /**
     * Sets the Auth0 domain.
     *
     * @param domain the Auth0 domain to set.
     */
    public void setDomain(String domain) {
        this.domain = domain;
    }

    /**
     * Returns the Auth0 client ID.
     *
     * @return the Auth0 client ID.
     */
    public String getClientId() {
        return clientId;
    }

    /**
     * Sets the Auth0 client ID.
     *
     * @param clientId the Auth0 client ID to set.
     */
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    /**
     * Returns the Auth0 client secret.
     *
     * @return the Auth0 client secret.
     */
    public String getClientSecret() {
        return clientSecret;
    }

    /**
     * Sets the Auth0 client secret.
     *
     * @param clientSecret the Auth0 client secret to set.
     */
    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    /**
     * Returns the role claim URL.
     *
     * @return the role claim URL.
     */
    public String getRoleClaim() {
        return "https://" + domain + "/roles";
    }
}