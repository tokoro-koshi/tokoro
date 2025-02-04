package com.tokorokoshi.tokoro.configuration;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * Configures the MongoDB connection.
 */
@Configuration
@EnableMongoAuditing
public class MongoConfiguration {
    @Value("${spring.data.mongodb.database}")
    private String databaseName;

    @Value("${spring.data.mongodb.uri}")
    private String uri;

    /**
     * A bean that provides the database connection.
     * @return A new MongoDB client
     */
    @Bean
    public MongoClient client() {
        ConnectionString connectionString = new ConnectionString(uri);
        MongoClientSettings mongoClientSettings = MongoClientSettings
                .builder()
                .applyConnectionString(connectionString)
                .build();
        return MongoClients.create(mongoClientSettings);
    }

    /**
     * A bean that provides the MongoDB template.
     * @param mongoClient The MongoDB client
     * @return A new MongoDB template
     */
    @Bean
    public MongoTemplate template(MongoClient mongoClient) {
        return new MongoTemplate(mongoClient, databaseName);
    }
}
