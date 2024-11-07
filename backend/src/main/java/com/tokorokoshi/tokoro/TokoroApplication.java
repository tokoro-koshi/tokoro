package com.tokorokoshi.tokoro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TokoroApplication {
    public static void main(String[] args) {
        System.out.println("Hello, Tokoro!");
        SpringApplication.run(TokoroApplication.class, args);
    }
}
