package com.artclassmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class ArtClassManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(ArtClassManagementApplication.class, args);
    }
}
