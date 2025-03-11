package com.tokorokoshi.tokoro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.metrics.MetricsAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoReactiveDataAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * The main class for the Tokoro application.
 * Here resides the main method that starts the Spring Boot application.
 */
@SpringBootApplication(
        exclude = {
                MongoReactiveDataAutoConfiguration.class,
                MetricsAutoConfiguration.class,
                DataSourceAutoConfiguration.class,
        },
        scanBasePackages = "com.tokorokoshi.tokoro"
)
public class TokoroApplication {
    /**
     * Entry point for the Tokoro application.
     *
     * @param args command-line arguments if any
     */
    public static void main(String[] args) {
        SpringApplication.run(TokoroApplication.class, args);
    }
}
