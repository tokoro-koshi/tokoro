package com.tokorokoshi.tokoro.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Objects;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    private final String[] activeProfiles;

    private final CorsConfigurationSource corsConfigurationSource;

    @Value("${AUTH0_ISSUER_BASE_URL}")
    private String issuer;

    @Value("${AUTH0_AUDIENCE}")
    private String audience;


    @Autowired
    public SecurityConfiguration(
            Environment env,
            CorsConfigurationSource corsConfigurationSource
    ) {
        this.activeProfiles = env.getActiveProfiles();
        this.corsConfigurationSource = corsConfigurationSource;
    }

    private boolean isDevelopment() {
        return Arrays.asList(activeProfiles).contains("dev");
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {
        if (isDevelopment()) {
            http.cors(cors -> cors.configurationSource(corsConfigurationSource))
                    .authorizeHttpRequests(
                            auth -> auth.anyRequest().permitAll()
                    )
                    .csrf(AbstractHttpConfigurer::disable);
        } else {
            // Production & staging configuration
            http.cors(cors -> cors.configurationSource(corsConfigurationSource))
                    .oauth2ResourceServer(
                            oauth2 -> oauth2.jwt(
                                    jwt -> jwt.decoder(jwtDecoder())
                            )
                    )
                    .authorizeHttpRequests(
                            auth -> auth.requestMatchers("/error")
                                    .permitAll()
                                    .requestMatchers("/actuator/health")
                                    .permitAll()
                                    .requestMatchers(HttpMethod.OPTIONS, "/**")
                                    .permitAll()
                                    .requestMatchers("/api/**")
                                    .authenticated()
                                    .anyRequest()
                                    .denyAll()
                    )
                    .csrf(AbstractHttpConfigurer::disable);
        }
        return http.build();
    }

    @Bean
    @Profile("prod")
    public JwtDecoder jwtDecoder() {
        NimbusJwtDecoder jwtDecoder = JwtDecoders
                .fromOidcIssuerLocation(Objects.requireNonNull(issuer));

        OAuth2TokenValidator<Jwt> audienceValidator
                = new AudienceValidator(audience);
        OAuth2TokenValidator<Jwt> withIssuer
                = JwtValidators.createDefaultWithIssuer(issuer);
        OAuth2TokenValidator<Jwt> withAudience
                = new DelegatingOAuth2TokenValidator<>(
                withIssuer,
                audienceValidator
        );

        jwtDecoder.setJwtValidator(withAudience);

        return jwtDecoder;
    }
}