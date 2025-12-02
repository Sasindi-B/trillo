package com.trillo.app.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.trillo.app.entities.Payment;

public interface PaymentRepository extends MongoRepository<Payment, String> {
    Optional<Payment> findByBookingId(String bookingId);
}
