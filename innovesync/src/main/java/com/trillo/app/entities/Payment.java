package com.trillo.app.entities;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "app_payment")
public class Payment extends BaseEntity {
    private String bookingId;
    private String travellerId;
    private double amount;
    private PaymentStatus status = PaymentStatus.UNPAID;

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getTravellerId() {
        return travellerId;
    }

    public void setTravellerId(String travellerId) {
        this.travellerId = travellerId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }
}
