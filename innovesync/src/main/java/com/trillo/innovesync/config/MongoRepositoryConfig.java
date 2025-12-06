package com.trillo.innovesync.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Explicitly enable Mongo repositories across both legacy and new packages.
 */
@Configuration
@EnableMongoRepositories(basePackages = {
        "com.trillo.innovesync.businessowner",
        "com.trillo.innovesync.traveller",
        "com.trillo.innovesync.auth",
        "com.trillo.innovesync.membership",
        "com.trillo.app.repositories"
})
public class MongoRepositoryConfig {
}
