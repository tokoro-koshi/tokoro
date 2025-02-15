package com.tokorokoshi.tokoro.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "Tokoro API",
                version = "1.0",
                description = "This API provides the functionality to fullfill the Tokoro application needs in data querying and manipulation."
        )
)
public class SwaggerConfiguration {
}
