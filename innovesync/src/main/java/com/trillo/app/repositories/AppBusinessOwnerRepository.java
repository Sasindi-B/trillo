package com.trillo.app.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.trillo.app.entities.BusinessOwner;

public interface AppBusinessOwnerRepository extends MongoRepository<BusinessOwner, String> {
    Optional<BusinessOwner> findByEmailIgnoreCase(String email);
}
