package com.tokorokoshi.tokoro.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Objects;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final Environment env;
    private final CorsConfigurationSource corsConfigurationSource;

    @Autowired
    SecurityConfig(Environment env, CorsConfigurationSource corsConfigurationSource) {
        this.env = env;
        this.corsConfigurationSource = corsConfigurationSource;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        if (isProduction()) {
            http
                    .cors(cors -> cors.configurationSource(corsConfigurationSource))
                    .oauth2ResourceServer(oauth2 -> oauth2
                            .jwt(jwt -> jwt.decoder(jwtDecoder()))
                    );

            http.authorizeHttpRequests(auth -> auth
                    .requestMatchers("/error").permitAll()
                    .requestMatchers("/actuator/health").permitAll()
                    .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                    .requestMatchers("/api/**").authenticated()
                    .anyRequest().denyAll()
            );
        } else {
            http
                    .cors(cors -> cors.configurationSource(corsConfigurationSource))
                    .authorizeHttpRequests(auth -> auth
                            .anyRequest().permitAll()
                    );
        }

        http.csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

    private boolean isProduction() {
        return Arrays.stream(env.getActiveProfiles()).anyMatch("prod"::equals);
    }

    @Bean
    @Profile("prod")
    JwtDecoder jwtDecoder() {
        String issuer = env.getProperty("auth0.issuer");
        String audience = env.getProperty("auth0.audience");

        NimbusJwtDecoder jwtDecoder = JwtDecoders.fromOidcIssuerLocation(Objects.requireNonNull(issuer));

        OAuth2TokenValidator<Jwt> audienceValidator = new AudienceValidator(audience);
        OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(issuer);
        OAuth2TokenValidator<Jwt> withAudience = new DelegatingOAuth2TokenValidator<>(withIssuer, audienceValidator);

        jwtDecoder.setJwtValidator(withAudience);

        return jwtDecoder;
    }
}