package com.trillo.app.dtos;

import java.time.Instant;

import com.trillo.app.entities.PaymentStatus;

public class PaymentResponse {
    private String bookingId;
    private String travellerId;
    private double amount;
    private PaymentStatus status;
    private Instant createdAt;

    public PaymentResponse(String bookingId, String travellerId, double amount, PaymentStatus status, Instant createdAt) {
        this.bookingId = bookingId;
        this.travellerId = travellerId;
        this.amount = amount;
        this.status = status;
        this.createdAt = createdAt;
    }

    public String getBookingId() {
        return bookingId;
    }

    public String getTravellerId() {
        return travellerId;
    }

    public double getAmount() {
        return amount;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
