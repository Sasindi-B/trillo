package com.trillo.innovesync.membership;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface MembershipRepository extends MongoRepository<Membership, String> {
    Optional<Membership> findByBusinessOwnerId(String businessOwnerId);

    Optional<Membership> findByBusinessOwnerIdAndIsActiveTrue(String businessOwnerId);

    List<Membership> findAllByIsActiveTrue();

    List<Membership> findAllByBusinessOwnerId(String businessOwnerId);
}
