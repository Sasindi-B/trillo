package com.trillo.innovesync.businessowner;

public record BusinessOwnerSignupResponse(
        String id,
        String businessName,
        String email,
        String location,
        String category) {
}
