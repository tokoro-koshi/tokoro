package com.tokorokoshi.tokoro.configuration;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;

/**
 * Configuration class for the application.
 * <p>
 * This class is used to load the application properties from the application.properties and secrets.properties files.
 * </p>
 * <p>
 * Also, this class is used to enable caching in the application.
 * The caching is used to store the results of expensive operations so that they can be reused later.
 * This can help improve the performance of the application by reducing the time taken to perform these operations.
 * The caching is done using the Spring Cache abstraction, which provides a simple and consistent way to cache data in the application.
 * </p>
 */
@Configuration
@EnableCaching
@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
@PropertySources(
        {
                @PropertySource(
                        value = "classpath:application.properties",
                        ignoreResourceNotFound = true
                ),
                @PropertySource(
                        value = "classpath:secrets.properties",
                        ignoreResourceNotFound = true
                )
        }
)
public class ApplicationConfiguration {
}
