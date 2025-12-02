package com.trillo.app.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.trillo.app.entities.BusinessProfile;

public interface BusinessProfileRepository extends MongoRepository<BusinessProfile, String> {
    Optional<BusinessProfile> findByOwnerId(String ownerId);
}
