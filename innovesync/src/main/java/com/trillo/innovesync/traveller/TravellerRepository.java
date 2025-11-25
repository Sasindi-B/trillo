package com.trillo.innovesync.traveller;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface TravellerRepository extends MongoRepository<Traveller, String> {
    boolean existsByEmailIgnoreCase(String email);
    java.util.Optional<Traveller> findByEmailIgnoreCase(String email);
}
