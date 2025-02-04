package com.tokorokoshi.tokoro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The main class for the Tokoro application.
 * Here resides the main method that starts the Spring Boot application.
 */
@SpringBootApplication
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
