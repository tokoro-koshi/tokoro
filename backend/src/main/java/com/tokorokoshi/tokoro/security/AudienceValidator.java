package com.tokorokoshi.tokoro.security;

import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;

/**
 * Validates the audience of a JWT.
 */
public class AudienceValidator implements OAuth2TokenValidator<Jwt> {
    private final String audience;

    /**
     * Creates a new instance of the AudienceValidator class.
     *
     * @param audience The audience to validate.
     */
    public AudienceValidator(String audience) {
        this.audience = audience;
    }

    /**
     * Validates the audience of the given JWT.
     *
     * @param jwt The JWT to validate.
     * @return The result of the validation.
     */
    @Override
    public OAuth2TokenValidatorResult validate(Jwt jwt) {
        if (jwt.getAudience().contains(audience)) {
            return OAuth2TokenValidatorResult.success();
        }
        return OAuth2TokenValidatorResult.failure(
                new OAuth2Error(
                        "invalid_token",
                        "The required audience is missing",
                        null
                )
        );
    }
}