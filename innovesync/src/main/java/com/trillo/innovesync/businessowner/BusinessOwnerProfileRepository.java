package com.trillo.innovesync.businessowner;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface BusinessOwnerProfileRepository extends MongoRepository<BusinessOwnerProfile, String> {
    Optional<BusinessOwnerProfile> findByOwnerId(String ownerId);

    boolean existsByOwnerId(String ownerId);
}
