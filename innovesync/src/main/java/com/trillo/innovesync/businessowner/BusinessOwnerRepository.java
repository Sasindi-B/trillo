package com.trillo.innovesync.businessowner;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface BusinessOwnerRepository extends MongoRepository<BusinessOwner, String> {
    boolean existsByEmailIgnoreCase(String email);

    Optional<BusinessOwner> findByEmailIgnoreCase(String email);
}
