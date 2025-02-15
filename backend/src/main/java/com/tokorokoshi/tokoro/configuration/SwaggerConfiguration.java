package com.tokorokoshi.tokoro.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Tokoro API",
                version = "v1",
                description = "This API provides the functionality to fullfill the Tokoro application needs in data querying and manipulation."
        ),
        security = {
                @io.swagger.v3.oas.annotations.security.SecurityRequirement(
                        name = "bearerAuth"
                )
        }
)
public class SwaggerConfiguration {
    private static final String securitySchemeName = "bearerAuth";
    private static final String description = "Enter the JWT token with the `Bearer ` prefix, e.g. \"Bearer eyJhbGci...\"";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .addSecurityItem(
                        new SecurityRequirement().addList(securitySchemeName)
                )
                .components(new Components().addSecuritySchemes(
                                securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("Bearer")
                                        .bearerFormat("JWT")
                                        .description(description)
                        )
                );
    }
}
