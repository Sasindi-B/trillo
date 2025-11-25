package com.trillo.innovesync.traveller;

import java.util.List;

public record TravellerSignupResponse(
        String id,
        String fullName,
        String email,
        String city,
        List<String> interests
) {
}
