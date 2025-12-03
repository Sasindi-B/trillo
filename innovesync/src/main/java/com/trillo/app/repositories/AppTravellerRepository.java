package com.trillo.app.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.trillo.app.entities.Traveller;

public interface AppTravellerRepository extends MongoRepository<Traveller, String> {
    Optional<Traveller> findByEmailIgnoreCase(String email);
}


