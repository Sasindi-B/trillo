package com.trillo.app.dtos;

import java.time.Instant;
import java.time.LocalDate;

import com.trillo.app.entities.BookingStatus;

public class BookingResponse {
    private String id;
    private String businessId;
    private String travellerId;
    private LocalDate startDate;
    private LocalDate endDate;
    private int numberOfPeople;
    private BookingStatus status;
    private Instant createdAt;

    public BookingResponse(String id, String businessId, String travellerId, LocalDate startDate, LocalDate endDate,
            int numberOfPeople, BookingStatus status, Instant createdAt) {
        this.id = id;
        this.businessId = businessId;
        this.travellerId = travellerId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.numberOfPeople = numberOfPeople;
        this.status = status;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public String getBusinessId() {
        return businessId;
    }

    public String getTravellerId() {
        return travellerId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public int getNumberOfPeople() {
        return numberOfPeople;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
