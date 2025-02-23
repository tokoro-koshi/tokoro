package com.tokorokoshi.tokoro.configuration;

import com.tokorokoshi.tokoro.security.AudienceValidator;
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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Configures the security settings.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    private final String[] activeProfiles;

    private final CorsConfigurationSource corsConfigurationSource;

    @Value("${AUTH0_ISSUER_BASE_URL}")
    private String issuer;

    @Value("${AUTH0_AUDIENCE}")
    private String audience;

    @Value("${role_claims}")
    private String ROLE_CLAIMS;

    /**
     * Creates a new instance of the SecurityConfiguration class.
     *
     * @param env                     The environment
     * @param corsConfigurationSource The CORS configuration source
     */
    @Autowired
    public SecurityConfiguration(
            Environment env,
            CorsConfigurationSource corsConfigurationSource
    ) {
        this.activeProfiles = env.getActiveProfiles();
        this.corsConfigurationSource = corsConfigurationSource;
    }

    /**
     * Checks if the current profile is development.
     *
     * @return true if the active profile is "dev", false otherwise
     */
    private boolean isDevelopment() {
        return Arrays.asList(activeProfiles).contains("dev");
    }

    /**
     * Configures the security settings.
     *
     * @param http The HTTP security
     * @return The security filter chain
     * @throws Exception if there is an error configuring the security settings
     */
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
                                    jwt -> jwt.decoder(jwtDecoder()).jwtAuthenticationConverter(jwtAuthenticationConverter())
                            )
                    )
                    .authorizeHttpRequests(
                            auth -> auth.requestMatchers("/error")
                                    .permitAll()
                                    .requestMatchers(HttpMethod.OPTIONS, "/**")
                                    .permitAll()
                                    .requestMatchers("/actuator/**")
                                    .permitAll()
                                    .requestMatchers(
                                            HttpMethod.GET,
                                            "/blogs/**",
                                            "/privacy/**",
                                            "/about/**",
                                            "/features/**",
                                            "/places/**",
                                            "/testimonials/**",
                                            "/reviews/**"
                                    )
                                    .permitAll()
                                    .requestMatchers(
                                            "/swagger-ui/**",
                                            "/swagger-ui.html",
                                            "/v3/api-docs/**",
                                            "/swagger-resources/**"
                                    )
                                    .permitAll()
                                    .requestMatchers("/**")
                                    .authenticated()
                                    .anyRequest()
                                    .denyAll()
                    )
                    .csrf(AbstractHttpConfigurer::disable);
        }
        return http.build();
    }

    /**
     * Creates a JWT authentication converter.
     *
     * @return A new JWT authentication converter
     */
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(this::extractAuthorities);
        return converter;
    }

    /**
     * Extracts authorities from the JWT claims.
     *
     * @param jwt The JWT token
     * @return A collection of granted authorities
     */
    @SuppressWarnings("unchecked")
    private Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
        Map<String, Object> claims = jwt.getClaims();

        List<String> roles = (List<String>) claims.getOrDefault(ROLE_CLAIMS, List.of());

        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                .collect(Collectors.toList());
    }

    /**
     * Creates a new JWT decoder.
     *
     * @return A new JWT decoder
     */
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