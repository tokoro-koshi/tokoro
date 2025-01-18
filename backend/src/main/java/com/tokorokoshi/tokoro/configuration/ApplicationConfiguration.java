package com.tokorokoshi.tokoro.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@PropertySources({
    @PropertySource(value = "classpath:application.properties", ignoreResourceNotFound = true),
    @PropertySource(value = "classpath:secrets.properties", ignoreResourceNotFound = true)
})
public class ApplicationConfiguration {
}
